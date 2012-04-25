/**
 * The MIT License
 *
 * Copyright 2006-2011 The Codehaus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.codehaus.mojo.appassembler.daemon;

import java.io.File;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.appassembler.model.Daemon;

/**
 * @author <a href="mailto:trygve.laugstol@objectware.no">Trygve Laugst&oslash;l</a>
 * @version $Id: DaemonGenerationRequest.java 15699 2012-01-06 19:07:03Z khmarbaise $
 */
/**
 * @author kama
 *
 */
public class DaemonGenerationRequest
{
    private String platform;

    private File stubDescriptor;

    // TODO: what is the difference?
    private Daemon stubDaemon;

    private Daemon daemon;

    private File outputDirectory;

    private String binFolder;

    private MavenProject mavenProject;

    private ArtifactRepository localRepository;

    private ArtifactRepositoryLayout repositoryLayout = new DefaultRepositoryLayout ( );

    public DaemonGenerationRequest ( )
    {
    }

    /**
     * Request with the given parameters.
     * @param daemon The Daemon to use.
     * @param project The Maven Project
     * @param localRepository The local repository.
     * @param outputDir The output directory.
     * @param binFolder The binary folder.
     */
    public DaemonGenerationRequest ( Daemon daemon, MavenProject project, ArtifactRepository localRepository,
            File outputDir, String binFolder )
    {
        this.daemon = daemon;

        this.mavenProject = project;

        this.localRepository = localRepository;

        this.outputDirectory = outputDir;

        this.binFolder = binFolder;
    }

    /**
     * Get the Plaform.
     * @return the Platform.
     */
    public String getPlatform ()
    {
        return platform;
    }

    /**
     * @param platform Set the platform.
     */
    public void setPlatform ( String platform )
    {
        this.platform = platform;
    }

    /**
     * Get the StubDescriptor
     * FIXME: What for is this needed?
     * @return The Stub Descriptor file.
     */
    public File getStubDescriptor ()
    {
        return stubDescriptor;
    }

    /**
     * Set the StubDescriptor.
     * FIXME: What for is this needed?
     * @param stubDescriptor
     */
    public void setStubDescriptor ( File stubDescriptor )
    {
        this.stubDescriptor = stubDescriptor;
    }

    /**
     * Get the StubDaemon
     * FIXME: Is this needed?
     * @return The set stub Daemon
     */
    public Daemon getStubDaemon ()
    {
        return stubDaemon;
    }

    /**
     * Set the StubDaemon.
     * FIXME: Is this needed?
     * @param stubDaemon This will be set.
     */
    public void setStubDaemon ( Daemon stubDaemon )
    {
        this.stubDaemon = stubDaemon;
    }

    /**
     * Get the Daemon of the current request.
     * @return
     */
    public Daemon getDaemon ()
    {
        return daemon;
    }

    /**
     * Set the daemon.
     * @param daemon
     */
    public void setDaemon ( Daemon daemon )
    {
        this.daemon = daemon;
    }

    /**
     * Get the current outputDirectory.
     * @return
     */
    public File getOutputDirectory ()
    {
        return outputDirectory;
    }

    /**
     * Set the current output directory.
     * @param outputDirectory
     */
    public void setOutputDirectory ( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Get the use MavenProject.
     * @return
     */
    public MavenProject getMavenProject ()
    {
        return mavenProject;
    }

    /**
     * Set the Maven Project.
     * @param mavenProject
     */
    public void setMavenProject ( MavenProject mavenProject )
    {
        this.mavenProject = mavenProject;
    }

    /**
     * Get the local repository.
     * @return
     */
    public ArtifactRepository getLocalRepository ()
    {
        return localRepository;
    }

    /**
     * @param localRepository Set the local repositoy.
     */
    public void setLocalRepository ( ArtifactRepository localRepository )
    {
        this.localRepository = localRepository;
    }

    /**
     * @return The current repository layout.
     */
    public ArtifactRepositoryLayout getRepositoryLayout ()
    {
        return repositoryLayout;
    }

    /**
     * Set the current repository layout.
     * @param repositoryLayout The repositoryLayout which will be set.
     */
    public void setRepositoryLayout ( ArtifactRepositoryLayout repositoryLayout )
    {
        this.repositoryLayout = repositoryLayout;
    }

    /**
     * Get the current binary folder.
     * @return the setting of the binary folder.
     */
    public String getBinFolder ()
    {
        return binFolder;
    }

    /**
     * Set the binary folder.
     * @param binFolder 
     */
    public void setBinFolder ( String binFolder )
    {
        this.binFolder = binFolder;
    }

}
