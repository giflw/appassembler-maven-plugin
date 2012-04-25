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
package org.codehaus.mojo.appassembler.daemon.script;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.mojo.appassembler.daemon.DaemonGeneratorException;
import org.codehaus.mojo.appassembler.model.ClasspathElement;
import org.codehaus.mojo.appassembler.model.Daemon;
import org.codehaus.mojo.appassembler.model.Dependency;
import org.codehaus.mojo.appassembler.model.Directory;
import org.codehaus.mojo.appassembler.model.JvmSettings;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author <a href="mailto:trygve.laugstol@objectware.no">Trygve Laugst&oslash;l</a>
 * @version $Id: Platform.java 15699 2012-01-06 19:07:03Z khmarbaise $
 */
public class Platform
{
    /**
     * Unix as Platform name.
     */
    public static final String UNIX_NAME = "unix";

    /**
     * Windows Platform name.
     */
    public static final String WINDOWS_NAME = "windows";

    private static final Map ALL_PLATFORMS;

    private static final String DEFAULT_UNIX_BIN_FILE_EXTENSION = "";

    private static final String DEFAULT_WINDOWS_BIN_FILE_EXTENSION = ".bat";

    private String binFileExtension;

    private String name;

    private boolean isWindows;

    // -----------------------------------------------------------------------
    // Static
    // -----------------------------------------------------------------------

    static
    {
        ALL_PLATFORMS = new HashMap ( );
        addPlatform ( new Platform ( UNIX_NAME, false, DEFAULT_UNIX_BIN_FILE_EXTENSION ) );
        addPlatform ( new Platform ( WINDOWS_NAME, true, DEFAULT_WINDOWS_BIN_FILE_EXTENSION ) );
    }

    private static Platform addPlatform ( Platform platform )
    {
        ALL_PLATFORMS.put ( platform.name, platform );

        return platform;
    }

    /**
     * Get an instance of the named platform.
     * @param platformName The name of the wished platform.
     * @return Instance of the platform.
     * @throws DaemonGeneratorException in case of an wrong platformname.
     */
    public static Platform getInstance ( String platformName )
            throws DaemonGeneratorException
    {
        Platform platform = ( Platform ) ALL_PLATFORMS.get ( platformName );

        if ( platform == null )
        {
            throw new DaemonGeneratorException ( "Unknown platform name '" + platformName + "'" );
        }

        return platform;
    }

    /**
     * Get the names of all available platforms.
     * @return The names of the platform.
     */
    public static Set getAllPlatformNames ()
    {
        return ALL_PLATFORMS.keySet ( );
    }

    /**
     * Get all platforms.
     * @return All platforms.
     */
    public static Set getAllPlatforms ()
    {
        return new HashSet ( ALL_PLATFORMS.values ( ) );
    }

    /**
     * Redefine the list of platforms with the given one.
     * @param platformList The new list of platforms.
     * @return 
     * @throws DaemonGeneratorException in case of an error.
     */
    public static Set getPlatformSet ( List platformList )
            throws DaemonGeneratorException
    {
        return getPlatformSet ( platformList, new HashSet ( ALL_PLATFORMS.values ( ) ) );
    }

    /**
     * Get back all platforms.
     * @param platformList
     * @param allSet
     * @return
     * @throws DaemonGeneratorException
     */
    public static Set getPlatformSet ( List platformList, Set allSet )
            throws DaemonGeneratorException
    {
        if ( platformList == null )
        {
            return allSet;
        }

        if ( platformList.size ( ) == 1 )
        {
            Object first = platformList.get ( 0 );

            if ( "all".equals ( first ) )
            {
                return allSet;
            }

            throw new DaemonGeneratorException (
                    "The special platform 'all' can only be used if it is the only element in the platform list." );
        }

        Set platformSet = new HashSet ( );

        for ( Iterator it = platformList.iterator ( ); it.hasNext ( ); )
        {
            String platformName = ( String ) it.next ( );

            if ( platformName.equals ( "all" ) )
            {
                throw new DaemonGeneratorException (
                        "The special platform 'all' can only be used if it is the only element in a platform list." );
            }

            platformSet.add ( getInstance ( platformName ) );
        }

        return platformSet;
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private Platform ( String name, boolean isWindows, String binFileExtension )
    {
        this.name = name;

        this.isWindows = isWindows;

        this.binFileExtension = binFileExtension;
    }

    // -----------------------------------------------------------------------
    // The platform-specific bits
    // -----------------------------------------------------------------------

    /**
     * The interpolation token either for windows or unix.
     * @return The token which is used.
     */
    public String getInterpolationToken ()
    {
        return isWindows ? "#" : "@";
    }

    /**
     * @return The binary extension.
     */
    public String getBinFileExtension ()
    {
        return binFileExtension;
    }

    /**
     * @return BASEDIR representation for windows or unix.
     */
    public String getBasedir ()
    {
        return isWindows ? "\"%BASEDIR%\"" : "\"$BASEDIR\"";
    }

    /**
     * @return REPO representation for windows or unix.
     */
    public String getRepo ()
    {
        return isWindows ? "\"%REPO%\"" : "\"$REPO\"";
    }

    /**
     * @return The separator for windows or unix.
     */
    public String getSeparator ()
    {
        return isWindows ? "\\" : "/";
    }

    /**
     * @return The path separator for windows or unix.
     */
    public String getPathSeparator ()
    {
        return isWindows ? ";" : ":";
    }

    /**
     * @return Comment prefix for windows or unix.
     */
    public String getCommentPrefix ()
    {
        return isWindows ? "@REM " : "# ";
    }

    public String getNewLine ()
    {
        return isWindows ? "\r\n" : "\n";
    }

    // -----------------------------------------------------------------------
    // This part depend on the platform-specific parts
    // -----------------------------------------------------------------------

    /**
     * Get the ClassPath based on the given Daemon.
     * 
     * @param daemon 
     * @return The classpath as a string.
     * @throws DaemonGeneratorException
     */
    public String getClassPath ( Daemon daemon )
            throws DaemonGeneratorException
    {
        List classpath = daemon.getAllClasspathElements ( );

        StringBuffer classpathBuffer = new StringBuffer ( );

        for ( Iterator it = classpath.iterator ( ); it.hasNext ( ); )
        {
            if ( classpathBuffer.length ( ) > 0 )
            {
                classpathBuffer.append ( getPathSeparator ( ) );
            }

            // -----------------------------------------------------------------------
            //
            // -----------------------------------------------------------------------

            Object object = it.next ( );

            if ( object instanceof Directory )
            {
                Directory directory = ( Directory ) object;

                if ( directory.getRelativePath ( ).charAt ( 0 ) != '/' )
                {
                    classpathBuffer.append ( getBasedir ( ) ).append ( getSeparator ( ) );
                }
            }
            else if ( object instanceof Dependency )
            {
                classpathBuffer.append ( getRepo ( ) ).append ( getSeparator ( ) );
            }
            else
            {
                throw new DaemonGeneratorException ( "Unknown classpath element type: " + object.getClass ( ).getName ( ) );
            }

            classpathBuffer.append ( StringUtils.replace ( ( ( ClasspathElement ) object ).getRelativePath ( ),
                    "/", getSeparator ( ) ) );
        }

        return classpathBuffer.toString ( );
    }

    private String interpolateBaseDirAndRepo ( String content )
    {
        StringReader sr = new StringReader ( content );
        StringWriter result = new StringWriter ( );

        Map context = new HashMap ( );

        context.put ( "BASEDIR", StringUtils.quoteAndEscape ( getBasedir ( ), '"' ) );
        context.put ( "REPO", StringUtils.quoteAndEscape ( getRepo ( ), '"' ) );
        InterpolationFilterReader interpolationFilterReader =
                new InterpolationFilterReader ( sr, context, "@", "@" );
        try
        {
            IOUtil.copy ( interpolationFilterReader, result );
        }
        catch ( IOException e )
        {
            // shouldn't happen...
        }
        return result.toString ( );
    }

    private List convertArguments ( List strings )
    {
        if ( strings == null )
        {
            return strings;
        }

        ArrayList result = new ArrayList ( );
        for ( Iterator iterator = strings.iterator ( ); iterator.hasNext ( ); )
        {
            String argument = ( String ) iterator.next ( );
            result.add ( interpolateBaseDirAndRepo ( argument ) );
        }

        return result;
    }

    /**
     * Get the extra JVMArguments.
     * @param jvmSettings
     * @return
     * @throws IOException
     */
    public String getExtraJvmArguments ( JvmSettings jvmSettings ) throws IOException
    {
        if ( jvmSettings == null )
        {
            return "";
        }

        String vmArgs = "";

        vmArgs = addJvmSetting ( "-Xms", jvmSettings.getInitialMemorySize ( ), vmArgs );
        vmArgs = addJvmSetting ( "-Xmx", jvmSettings.getMaxMemorySize ( ), vmArgs );
        vmArgs = addJvmSetting ( "-Xss", jvmSettings.getMaxStackSize ( ), vmArgs );

        vmArgs += arrayToString ( convertArguments ( jvmSettings.getExtraArguments ( ) ), "" );
        vmArgs += arrayToString ( jvmSettings.getSystemProperties ( ), "-D" );

        return vmArgs.trim ( );
    }

    private String arrayToString ( List strings, String separator )
    {
        String string = "";

        if ( strings != null )
        {
            Iterator it = strings.iterator ( );

            while ( it.hasNext ( ) )
            {
                String s = ( String ) it.next ( );

                if ( s.indexOf ( ' ' ) == -1 )
                {
                    string += " " + separator + s;
                }
                else
                {
                    string += " \"" + separator + s + "\"";
                }
            }
        }

        return string;
    }

    /**
     * Get the application arguments.
     * @param descriptor
     * @return The list of application arguments.
     */
    public String getAppArguments ( Daemon descriptor )
    {
        List commandLineArguments = convertArguments ( descriptor.getCommandLineArguments ( ) );

        if ( commandLineArguments == null || commandLineArguments.size ( ) == 0 )
        {
            return null;
        }

        if ( commandLineArguments.size ( ) == 1 )
        {
            return ( String ) commandLineArguments.get ( 0 );
        }

        Iterator it = commandLineArguments.iterator ( );

        String appArguments = ( String ) it.next ( );

        while ( it.hasNext ( ) )
        {
            appArguments += " " + it.next ( );
        }

        return appArguments;
    }

    private String addJvmSetting ( String argType, String extraJvmArgument, String vmArgs )
    {
        if ( StringUtils.isEmpty ( extraJvmArgument ) )
        {
            return vmArgs;
        }

        return vmArgs + " " + argType + extraJvmArgument;
    }

    /**
     * Get the environment setup file.
     * @param daemon
     * @return
     */
    public String getEnvSetup ( Daemon daemon )
    {
        String envSetup = "";

        String envSetupFileName = daemon.getEnvironmentSetupFileName ( );

        if ( envSetupFileName != null )
        {
            if ( isWindows )
            {
                String envScriptPath = "\"%BASEDIR%\\bin\\" + envSetupFileName + ".bat\"";

                envSetup = "if exist " + envScriptPath + " call " + envScriptPath;
            }
            else
            {
                String envScriptPath = "\"$BASEDIR\"/bin/" + envSetupFileName;
                envSetup = "[ -f " + envScriptPath + " ] && . " + envScriptPath;
            }
        }

        return envSetup;
    }

    // -----------------------------------------------------------------------
    // Object overrides
    // -----------------------------------------------------------------------

    public boolean equals ( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null || getClass ( ) != o.getClass ( ) )
        {
            return false;
        }

        Platform platform = ( Platform ) o;

        return name.equals ( platform.name );
    }

    public int hashCode ()
    {
        return name.hashCode ( );
    }

    public String getName ()
    {
        return name;
    }

    /**
     * ShowConsole window.
     * @param daemon
     * @return true yes false otherwise.
     */
    public boolean isShowConsoleWindow ( Daemon daemon )
    {
        return daemon.isShowConsoleWindow ( ) && isWindows;
    }

    // -----------------------------------------------------------------------
    // Setters for the platform-specific bits
    // -----------------------------------------------------------------------

    /**
     * Set the bin file extension.
     * @param binFileExtension
     */
    public void setBinFileExtension ( String binFileExtension )
    {
        // We can't have a null extension
        if ( binFileExtension == null )
        {
            this.binFileExtension = "";
        }
        else
        {
            this.binFileExtension = binFileExtension;
        }
    }
}
