import org.scalajs.linker.interface.ModuleSplitStyle

lazy val interactiveCommentSection = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    scalaVersion := "3.3.0",

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "livechart" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("livechart")))
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.4.0",
    // https://laminar.dev/documentation
    libraryDependencies += "com.raquo" %%% "laminar" % "15.0.1",
    // check out extras in laminext : fetch \ ws \ sources & signals
    // https://laminext.dev/v/0.15.x/
    libraryDependencies += "io.laminext" %%% "fetch" % "0.15.0",
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-time" % "2.5.0",
    libraryDependencies += "com.softwaremill.quicklens" %%% "quicklens" % "1.9.4",
    libraryDependencies += ("org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "com.lihaoyi" %%% "upickle" % "3.1.0",
  )
