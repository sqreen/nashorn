module io.sqreen.nashorn {
    requires jdk.unsupported;
    requires jdk.dynalink;
    requires java.logging;
    requires transitive java.scripting;
    requires io.sqreen.nashorn.asm;
    exports io.sqreen.nashorn.api.linker;
    exports io.sqreen.nashorn.api.scripting;
    exports io.sqreen.nashorn.api.tree;

    provides javax.script.ScriptEngineFactory
            with io.sqreen.nashorn.api.scripting.NashornScriptEngineFactory;

    provides jdk.dynalink.linker.GuardingDynamicLinkerExporter with
            io.sqreen.nashorn.api.linker.NashornLinkerExporter;

    // TODO: replace exports to .test with jvm parameters when running the tests
    exports io.sqreen.nashorn.internal.objects to
            io.sqreen.scripting.nashorn.shell,
            io.sqreen.nashorn.test;

    exports io.sqreen.nashorn.internal.runtime to
            io.sqreen.scripting.nashorn.shell,
            io.sqreen.nashorn.test;

    exports io.sqreen.nashorn.tools to
            io.sqreen.scripting.nashorn.shell,
            io.sqreen.nashorn.test;

    opens io.sqreen.nashorn.tools to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.parser to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.linker to io.sqreen.nashorn.test;
    opens io.sqreen.nashorn.internal.runtime to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.options to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.doubleconv to io.sqreen.nashorn.test;
    opens io.sqreen.nashorn.internal.runtime.doubleconv to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.regexp to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.regexp.joni to io.sqreen.nashorn.test;
    exports io.sqreen.nashorn.internal.runtime.events to io.sqreen.nashorn.test;
}
