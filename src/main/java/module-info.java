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

    exports io.sqreen.nashorn.internal.objects to io.sqreen.scripting.nashorn.shell;
    exports io.sqreen.nashorn.internal.runtime to io.sqreen.scripting.nashorn.shell;
    exports io.sqreen.nashorn.tools to io.sqreen.scripting.nashorn.shell;
}
