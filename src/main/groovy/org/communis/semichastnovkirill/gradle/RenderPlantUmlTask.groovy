package org.communis.semichastnovkirill.gradle

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.apache.commons.io.FilenameUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by simon on 14.01.2018.
 */
class RenderPlantUmlTask extends DefaultTask {

    @TaskAction
    def render() {
        Path projectPath = project.projectDir.toPath()
        Path assetsPath = projectPath.resolve(Paths.get(project.plantuml.sourcePath))
        Path buildPath = projectPath.resolve(Paths.get(project.plantuml.buildPath))

        assetsPath.toFile().eachDirRecurse() { dir ->
            dir.eachFileMatch(~/.*.puml/) { file ->
                String pumlContent = new String(Files.readAllBytes(file.toPath()), 'UTF-8')

                SourceStringReader reader

                reader = new SourceStringReader(pumlContent)
                Path destPath = getDestination(file, project.plantuml.fileFormat.getFileSuffix(), buildPath.resolve(assetsPath.relativize(file.toPath().getParent())))
                if(!destPath.getParent().toFile().exists())
                    if(!destPath.getParent().toFile().mkdirs())
                        System.err.println("mkdirs fail: ${destPath.getParent()}")

                println "Rendering ${file.toPath().toString()} to ${projectPath.relativize(destPath)}"
                reader.generateImage(new FileOutputStream(destPath.toFile()), new FileFormatOption(project.plantuml.fileFormat))
            }
        }
    }

    Path getDestination(File puml, String extension, Path buildPath) {
        puml.getParent()
        String baseName = FilenameUtils.getBaseName(puml.name)
        String destName = "${baseName}"
        buildPath.resolve(destName + extension)
    }
}