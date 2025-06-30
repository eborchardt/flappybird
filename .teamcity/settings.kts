import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubConnection

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.03"

project {

    buildType(BuildC)
    buildType(BuildB)
    buildType(CompositeAb)
    buildType(CompositeAc)
    buildType(BuildA)

    features {
        githubConnection {
            id = "PROJECT_EXT_3"
            displayName = "GitHub.com"
            clientId = "Ov23liOLrFdGNyFsZPIa"
            clientSecret = "credentialsJSON:1d96ed2c-0345-4683-9163-fffd298d4c72"
        }
    }
}

object BuildA : BuildType({
    name = "Build A"
    paused = true

    artifactRules = "target/gemini-1.0-SNAPSHOT.jar"

    vcs {
        root(DslContext.settingsRoot, "-:.idea", "+:*")
        root(AbsoluteId("ExampleProject_HttpsGithubComEborchardtSsd1306Scroll"))
    }

    steps {
        maven {
            id = "Maven2"
            goals = "clean test package"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            jdkHome = "%env.JDK_17_0_x64%"
        }
    }
})

object BuildB : BuildType({
    name = "Build B"
    paused = true

    params {
        param("VcsRevision", "${BuildA.depParamRefs["build.vcs.number"]}")
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = """echo "The build is using revision: %VcsRevision%""""
        }
    }

    dependencies {
        snapshot(BuildA) {
            synchronizeRevisions = false
        }
    }
})

object BuildC : BuildType({
    name = "Build C"
    paused = true

    params {
        param("DateTime", "")
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = """
                datetime=${'$'}(date +'%%Y-%%m-%%d-%%H:%%M')
                echo "##teamcity[setParameter name='DateTime' value='${'$'}datetime']"
            """.trimIndent()
        }
    }

    dependencies {
        snapshot(BuildA) {
        }
    }
})

object CompositeAb : BuildType({
    name = "Composite AB"
    paused = true

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        dependency(BuildA) {
            snapshot {
            }

            artifacts {
                artifactRules = "gemini-1.0-SNAPSHOT.jar => gemini-1.0-${BuildB.depParamRefs["VcsRevision"]}.jar"
            }
        }
        snapshot(BuildB) {
        }
    }
})

object CompositeAc : BuildType({
    name = "Composite AC"
    paused = true

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        dependency(BuildA) {
            snapshot {
                synchronizeRevisions = false
            }

            artifacts {
                artifactRules = "gemini-1.0-SNAPSHOT.jar => gemini-1.0-${BuildC.depParamRefs["DateTime"]}.jar"
            }
        }
        snapshot(BuildC) {
            synchronizeRevisions = false
        }
    }
})
