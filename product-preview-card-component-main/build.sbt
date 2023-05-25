import org.scalajs.linker.interface.ModuleSplitStyle

lazy val productPreviewCardComponent = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    scalaVersion := "3.2.2",

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
  )
