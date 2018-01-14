package org.communis.semichastnovkirill.gradle

import org.apache.commons.io.FilenameUtils
import org.gradle.api.tasks.Delete

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by simon on 14.01.2018.
 */
class CleanPlantUmlTask extends Delete {

    def Path assetsPath = Paths.get(project.plantuml.buildPath)

    CleanPlantUmlTask() {
        if(!Files.exists(assetsPath)) Files.createDirectory(assetsPath)

        for (Path puml : Files.newDirectoryStream(assetsPath, project.plantuml.fileFormat.getFileSuffix())) {
            Files.deleteIfExists(puml)
        }
    }
}