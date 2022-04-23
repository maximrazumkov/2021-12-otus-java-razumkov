package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.util.QueryFunction;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.BeanDefinitionException;
import ru.otus.exception.NotFoundBeanException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.ReflectionUtils.get;
import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;
import static ru.otus.helper.ReflectionHelper.instantiate;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packs) {
        processConfig(packs);
    }

    private void processConfig(String configClass) {
        List<Class<?>> annotatedClasses = getAnnotatedClasses(configClass);
        annotatedClasses.forEach(this::checkConfigClass);
        initAppContext(annotatedClasses);
    }

    private void initAppContext(List<Class<?>> annotatedClasses) {
        annotatedClasses.forEach(annotatedClass -> {
            List<Method> methods = getMethods(getQueryFunctionMethods(annotatedClass));
            Object instance = instantiate(annotatedClass);
            initAppComponents(instance, methods);
        });
    }

    private void initAppComponents(Object instance, List<Method> methods) {
        methods.forEach(method -> {
            String beanName = method.getAnnotation(AppComponent.class).name();
            validateBeanName(beanName);
            Object bean = callMethod(instance, method, getParametersMethod(method));
            appComponents.add(bean);
            appComponentsByName.put(beanName, bean);
        });
    }

    private void validateBeanName(String beanName) {
        if (appComponentsByName.containsKey(beanName)) {
            throw new BeanDefinitionException(String.format("Invalid bean definition with name %s. It's already exist.", beanName));
        }
    }

    private Object[] getParametersMethod(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .toArray();
    }

    private Object callMethod(Object instance, Method method, Object... parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Method> getMethods(QueryFunction<Store, Method> queryFunction) {
        return get(queryFunction)
                .stream()
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private QueryFunction<Store, Method> getQueryFunctionMethods(Class<?> annotatedClass) {
        return Methods.of(annotatedClass).filter(withAnnotation(AppComponent.class));
    }

    private List<Class<?>> getAnnotatedClasses(String configClass) {
        return new Reflections(configClass).get(getQueryFunctionClass())
                .stream()
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();
    }

    private QueryFunction<Store, Class<?>> getQueryFunctionClass() {
        return SubTypes.of(TypesAnnotated.with(AppComponentsContainerConfig.class)).asClass();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<?> components = getAppComponentByClass(componentClass);
        validateComponents(components, componentClass.getName());
        return (C) components.get(0);
    }

    private List<?> getAppComponentByClass(Class<?> componentClass) {
        return appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .toList();
    }

    private void validateComponents(List<?> components, String nameBean) {
        if (components.isEmpty()) {
            throw new NotFoundBeanException("Not found bean " + nameBean);
        }
        if (components.size() > 1) {
            throw new BeanDefinitionException(String.format("Bean name %s more than one.",nameBean));
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new NotFoundBeanException("Not found bean with name " + componentName);
    }
}
