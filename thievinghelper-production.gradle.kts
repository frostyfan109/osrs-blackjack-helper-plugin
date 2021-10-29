import ProjectVersions.openosrsVersion

version = "0.0.2"

project.extra["PluginName"] = "Thieving Helper Official Build"
project.extra["PluginDescription"] = "Failed knockout indicator for best xp rates"

dependencies {
    annotationProcessor(Libraries.lombok)
    annotationProcessor(Libraries.pf4j)

//    compileOnly("com.openosrs:runelite-api:$openosrsVersion")
//    compileOnly("com.openosrs:runelite-client:$openosrsVersion")

    implementation(files("D:\\openosrs-2021-fork\\runelite-api\\build\\libs\\runelite-api-4.9.10.jar"))
    implementation(files("D:\\openosrs-2021-fork\\runelite-client\\build\\libs\\runelite-client-4.9.10.jar"))
    implementation(files("D:\\openosrs-2021-fork\\runescape-api\\build\\libs\\runescape-api-4.9.10.jar"))
    implementation(files("D:\\openosrs-2021-fork\\runescape-client\\build\\libs\\runescape-client-4.9.10.jar"))


    compileOnly(Libraries.guice)
    compileOnly(Libraries.javax)
    compileOnly(Libraries.lombok)
    compileOnly(Libraries.pf4j)
    compileOnly(Libraries.rxjava)
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Plugin-Version" to project.version,
                    "Plugin-Id" to nameToId(project.extra["PluginName"] as String),
                    "Plugin-Provider" to project.extra["PluginProvider"],
                    "Plugin-Description" to project.extra["PluginDescription"],
                    "Plugin-License" to project.extra["PluginLicense"]
            ))
        }
    }
}