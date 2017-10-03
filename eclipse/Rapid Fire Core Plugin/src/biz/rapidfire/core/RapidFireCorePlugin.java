package biz.rapidfire.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RapidFireCorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "biz.rapidfire.core"; //$NON-NLS-1$

    // The shared instance
    private static RapidFireCorePlugin plugin;
    private static URL installURL;

    public static final String IMAGE_ERROR = "error.gif"; //$NON-NLS-1$

    /**
     * The constructor
     */
    public RapidFireCorePlugin() {
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);

        plugin = this;

        installURL = context.getBundle().getEntry("/"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static RapidFireCorePlugin getDefault() {
        return plugin;
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);

        reg.put(IMAGE_ERROR, getImageDescriptor(IMAGE_ERROR));
    }

    private ImageDescriptor getImageDescriptor(String name) {
        String iconPath = "icons/"; //$NON-NLS-1$
        try {
            URL url = new URL(installURL, iconPath + name);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            // should not happen
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

    /**
     * Convenience method to log error messages to the application log.
     * 
     * @param message Message
     * @param e The exception that has produced the error
     */
    public static void logError(String message, Throwable e) {
        if (plugin == null) {
            System.err.println(message);
            if (e != null) {
                e.printStackTrace();
            }
            return;
        }
        plugin.getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.ERROR, message, e));
    }

}
