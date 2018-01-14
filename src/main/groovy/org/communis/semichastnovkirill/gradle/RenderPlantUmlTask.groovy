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

    def Path assetsPath = Paths.get(project.plantuml.sourcePath)
    def Path buildPath = Paths.get(project.plantuml.buildPath)

    RenderPlantUmlTask() {
        if(Files.exists(assetsPath)) {
            for (Path puml : Files.newDirectoryStream(assetsPath, '*.puml')) {
                inputs.file puml.toFile()
            }

            for (Path puml : Files.newDirectoryStream(assetsPath, '*.puml')) {
                outputs.file getDestination(puml.toFile(), project.plantuml.fileFormat.getFileSuffix()).toFile()
            }
        }
    }

    Path getDestination(File puml, String extension) {
        String baseName = FilenameUtils.getBaseName(puml.name)
        String destName = "${baseName}"
        buildPath.resolve(destName + extension)
    }

    @TaskAction
    def render() {
        println "assetsPath ${assetsPath}"
        println "Files.exists(assetsPath) ${Files.exists(assetsPath)}"
        println "sourcePath ${project.plantuml.sourcePath}"

        Path projectPath = project.projectDir.toPath()

        for (File puml : inputs.files) {
            String relPumlPath = projectPath.relativize(puml.toPath()).toString()
            String pumlContent = new String(Files.readAllBytes(puml.toPath()), 'UTF-8')

            SourceStringReader reader

            reader = new SourceStringReader(pumlContent)
            Path destPath = getDestination(puml, project.plantuml.fileFormat.getFileSuffix())
            println "Rendering ${relPumlPath} to ${projectPath.relativize(destPath)}"
            reader.generateImage(new FileOutputStream(destPath.toFile()), new FileFormatOption(project.plantuml.fileFormat))
        }
    }
}