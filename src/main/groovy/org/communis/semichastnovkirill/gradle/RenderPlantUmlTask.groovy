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

/**
 * Created by simon on 14.01.2018.
 */
class RenderPlantUmlTask extends DefaultTask {

    @TaskAction
    def render() {
        Path projectPath = project.projectDir.toPath()
        Path assetsPath = projectPath.resolve(Paths.get(project.plantuml.sourcePath))
        Path buildPath = projectPath.resolve(Paths.get(project.plantuml.buildPath))

        println "projectPath ${projectPath}"
        println "assetsPath ${assetsPath}"
        println "buildPath ${buildPath}"

        for (Path puml : Files.newDirectoryStream(assetsPath, '*.puml')) {
            String pumlContent = new String(Files.readAllBytes(puml), 'UTF-8')

            SourceStringReader reader

            reader = new SourceStringReader(pumlContent)
            Path destPath = getDestination(puml.toFile(), project.plantuml.fileFormat.getFileSuffix(), buildPath)
            println "Rendering ${puml.toString()} to ${projectPath.relativize(destPath)}"
            reader.generateImage(new FileOutputStream(destPath.toFile()), new FileFormatOption(project.plantuml.fileFormat))
        }
    }

    Path getDestination(File puml, String extension, Path buildPath) {
        String baseName = FilenameUtils.getBaseName(puml.name)
        String destName = "${baseName}"
        buildPath.resolve(destName + extension)
    }
}