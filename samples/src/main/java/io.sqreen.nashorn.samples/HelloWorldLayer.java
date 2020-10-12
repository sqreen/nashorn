package io.sqreen.nashorn.samples;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Set;

class HelloWorldLayer {
    public static void main(String args[]) throws ScriptException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ModuleFinder finder = ModuleFinder.of(
                Path.of(System.getProperty("nashorn.jar")),
                Path.of(System.getProperty("asm.jar"))
        );

//        Stream<Path> list = Files.list(Paths.get(URI.create("jrt:/")));
//        list.forEach(System.out::println);

        ModuleLayer parent = ModuleLayer.boot();

//        Set<String> missingModules = finder.find("io.sqreen.nashorn")
//                .map(ModuleReference::descriptor)
//                .map(ModuleDescriptor::requires)
//                .orElse(Collections.emptySet())
//                .stream()
//                .map(ModuleDescriptor.Requires::name)
//                .filter(name -> parent.findModule(name).isEmpty() && finder.find(name).isEmpty())
//                .collect(Collectors.toSet());
//        missingModules.forEach(System.out::println);
//
//        Map<String, Path> systemModules = Files.list(Paths.get(URI.create("jrt:/")))
//                .collect(Collectors
//                        .toMap(x -> x.getFileName().toString(), Function.identity()));
//        systemModules.entrySet().stream().forEach(System.out::println);
//
//        var missingModulePaths = missingModules.stream()
//                .filter(systemModules::containsKey)
//                .map(systemModules::get)
//                .map(x -> Path.of(URI.create("jrt:/" + x.getFileName().toString())))
//                .toArray(Path[]::new);
//
//        for (Path missingModulePath : missingModulePaths) {
//            System.out.println(missingModulePath);
//        }
//
////        var missingModuleFinder = ModuleFinder.compose(ModuleFinder.ofSystem(), ModuleFinder.of(missingModulePaths));
////        var cfg = parent.configuration()
////                .resolveAndBind(missingModuleFinder, ModuleFinder.of(), missingModules);
////        ModuleLayer.defineModulesWithOneLoader(cfg, List.of(parent), null);
//
        Configuration cf = parent.configuration().resolve(
                ModuleFinder.compose(finder/*, ModuleFinder.ofSystem()*/),
                ModuleFinder.of(),
                Set.of("io.sqreen.nashorn", "io.sqreen.nashorn.asm"));

        ClassLoader scl = ClassLoader.getSystemClassLoader();
        ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);

//        new ScriptEngineManager().getEngineByName("nashorn");
//
//        ServiceLoader<ScriptEngineFactory> loaded =
//                ServiceLoader.load(layer, ScriptEngineFactory.class);
//        ScriptEngineFactory fac = loaded.findFirst().get();

        ScriptEngineFactory fac = (ScriptEngineFactory) layer.findLoader("io.sqreen.nashorn")
                .loadClass("io.sqreen.nashorn.api.scripting.NashornScriptEngineFactory")
                .getDeclaredConstructor().newInstance();
        ScriptEngine scriptEngine = fac.getScriptEngine();
        scriptEngine.eval("print('Hello world!')");
    }
}
