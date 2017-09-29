package biz.rapidfire.rse;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import biz.rapidfire.rse.subsystem.DeveloperAdapterFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class RapidFireRSEPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "biz.rapidfire.rse"; //$NON-NLS-1$

    // The shared instance
    private static RapidFireRSEPlugin plugin;

    /**
     * The constructor
     */
    public RapidFireRSEPlugin() {
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

        IAdapterManager manager = Platform.getAdapterManager();
        DeveloperAdapterFactory factory = new DeveloperAdapterFactory();
        // manager.registerAdapters(factory, samples.model.TeamResource.class);
        // manager.registerAdapters(factory,
        // samples.model.DeveloperResource.class); }
        manager.registerAdapters(factory, biz.rapidfire.rse.subsystem.RapidFireAdapter.class);
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
    public static RapidFireRSEPlugin getDefault() {
        return plugin;
    }

}
