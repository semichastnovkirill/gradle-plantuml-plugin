package org.communis.semichastnovkirill.gradle

import net.sourceforge.plantuml.FileFormat
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet

/**
 * Created by simon on 14.01.2018.
 */
class PlantUmlPluginExtension {
    def String sourcePath
    def String buildPath
    def SourceDirectorySet sources
    def FileFormat fileFormat
    def boolean overwrite

    PlantUmlPluginExtension(Project project) {
        sources = project.sourceSets.main.java
        sourcePath = 'src/main/java'
        buildPath = 'build/docs/uml'

        fileFormat = FileFormat.PNG
        overwrite = true
    }
}
