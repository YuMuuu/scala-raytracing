lazy val root = (project in file(".")).
  settings(
    name := "scala-raytracing",
    version := "0.1",
    scalaVersion := "2.13.1",
    scalacOptions := Seq(
      "-Ylog-classpath",
      "-target:jvm-1.8",
    ),
    libraryDependencies ++= Seq(
      //opengl
      "org.lwjgl" % "lwjgl" % "3.2.3",
      "org.lwjgl" % "lwjgl-glfw" % "3.2.3",
      "org.lwjgl" % "lwjgl-opengl" % "3.2.3",
      "org.lwjgl" % "lwjgl" % "3.2.3" classifier "natives-macos",
      "org.lwjgl" % "lwjgl-glfw" % "3.2.3" classifier "natives-macos",
      "org.lwjgl" % "lwjgl-opengl" % "3.2.3" classifier "natives-macos",

      //cats
      "org.typelevel" %% "cats-core" % "2.0.0",
    )
  )
//https://github.com/LWJGL/lwjgl3/issues/104
// unuse. add vmoption this
//javaOptions += "-XstartOnFirstThread"


