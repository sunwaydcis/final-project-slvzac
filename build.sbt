ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "Final Assignment Project",
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux")   => "linux"
        case n if n.startsWith("Mac")     => "mac"
        case n if n.startsWith("Windows") => "win"
        case _                            => throw new Exception("Unknown platform!")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "23.0.1" classifier osName)
    },
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafxml-core-sfx8" % "0.5",
      "org.scalafx" %% "scalafx" % "21.0.0-R30"
    ),
    javaOptions ++= Seq(
      "--module-path", "C:/Users/Zachcowy/Downloads/javafx-sdk-23.0.1/lib",
      "--add-modules", "javafx.controls,javafx.fxml"
    )
  )