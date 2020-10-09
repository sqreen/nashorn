module io.sqreen.nashorn.test {
    requires io.sqreen.nashorn;

    requires java.scripting;
    requires java.sql;
    requires org.testng;
    requires jdk.dynalink;
    requires java.management;

    uses javax.script.ScriptEngineFactory;

    provides java.sql.Driver with io.sqreen.nashorn.api.test.NashornSQLDriver;

    opens io.sqreen.nashorn.internal.test.framework;
    opens io.sqreen.nashorn.api.javaaccess.test;
    opens io.sqreen.nashorn.api.tree.test;
    opens io.sqreen.nashorn.api.scripting.test;
    opens io.sqreen.nashorn.internal.runtime.test;
    opens io.sqreen.nashorn.internal.runtime.test.nashorn;
    opens io.sqreen.nashorn.internal.runtime.doubleconv.test;
    opens io.sqreen.nashorn.internal.runtime.regexp.test;
    opens io.sqreen.nashorn.internal.runtime.regexp.joni.test;
    opens io.sqreen.nashorn.internal.runtime.linker.test;
    opens io.sqreen.nashorn.internal.codegen.test;
    opens io.sqreen.nashorn.internal.performance;
    opens io.sqreen.nashorn.internal.parser.test;
    opens io.sqreen.nashorn.test.models;
}
