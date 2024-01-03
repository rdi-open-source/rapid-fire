<html>
    <head>
        <meta http-equiv="cache-control" content="no-cache">
        <meta name="description" content="Rapid Fire Plug-in Update Site">
        <meta name="author" content="Rapid Fire Project Team">
        <meta name="keywords" content="Rapid Fire, Plugin, RDP, RDI, WDSCI, Eclipse, AS400">
        <link href="assets/stylesheet.css" rel="stylesheet" type="text/css" />
        <title>Rapid Fire Plug-in</title>
    </head>
    <body>
        <?php
            function parseJarManifest($manifestFileContents) {
               $manifest = array();	
               $lines = explode("\n", $manifestFileContents);
               foreach ($lines as $line) {
                  if (preg_match("/^([^:]+):\s*(.+)$/", $line, $m)) {
                     $manifest[$m[1]] = trim($m[2]);
                  }
               }
               return $manifest;
            }
            $notifier_url = str_replace( ' ' , '%20' , '@VERSION_MANIFEST@' );
            $manifestFileContents = file_get_contents( $notifier_url );
            $manifest = parseJarManifest($manifestFileContents);
            $current_version = $manifest['Bundle-Version'];
        ?>
      
        <table width="100%" border="0">
        <tr><td align="left" >
        <a href="https://sourceforge.net/projects/rapid-fire/"><img src="assets/rapidfire.png" alt="Rapid Fire Plug-in" height="79" border="0" style="padding-right: 10px;"/></a>
        </td>
        <td align="left" width="100%" >
        <h1>Rapid Fire Plug-in</h1>
        <p>Hi, this is the home of the Rapid Fire plug-in for RDi and WDSCi.</p>
        <p>Visit the <a target="_help" href="http://rapid-fire.sourceforge.net/help/">Rapid Fire help</a> site to find out more about the plug-in.</p>
        </td>
        <td valign="bottom" align="right" nowrap>
        <b>Version: <?php echo $current_version; ?></b>
        </td>
        </tr>
        </table>
        
        <h2 class="release">Introduction</h2>
        <div class="section">
		The Rapid Fire Open Source Project delivers a high quality and reliable solution for copying
		data while production is active.
		<p>
		Do you have files with millions or even billions of records? Imagine you have to change 
		such a file e.g. a new field has to be added. Possibly you have the time to install this 
		change at the weekend. But what if your shop works 24 hours a day and 7 days a week? In 
		this case it is absolutely necessary to reduce the time needed for the installation to 
		avoid a long shutdown of the system.
		<p>
		This is the point Rapid Fire comes in. With Rapid Fire it is possible to reduce the 
		downtime of your system to a minimum. Instead of shutting down the system for hours or 
		even days you can reduce this with Rapid Fire to minutes.
		<p>
		Rapid Fire features are driven from our ideas and needs, but everybody is encouraged to contribute 
		suggestions and manpower to improve the power of Rapid Fire.
		<p/>
		<table  border="0">
		<tr><td valign="top">
			<table border="0" style="border-spacing: 0px 0px; ">
			<tr><td valign="top"><h2>The Rapid Fire Project Team</h2></td></tr>
			<tr><td nowrap><a target="_owner" href="http://www.taskforce-it.de/"><img class="noborder" src="./assets/task-force.jpg" ></a><br>Task Force IT-Consulting GmbH, Frank Hildebrandt</td>
				<td width="60px"></td>
			    <td nowrap><a target="_owner" href="http://www.tools400.de/"><img class="noborder" src="./assets/tools400.png" ></a><br>Tools/400, Thomas Raddatz</td></tr>
	        </table>
		</td>
		<td width="60px"></td>
		<td valign="top">
			<!--
			<table border="0" style="border-spacing: 0px 0px; ">
			<tr><td valign="top"><h2>Contributors</h2></td></tr>
			<tr><td height="15px"></td></tr>
			<tr><td nowrap>Donald Duck</td><td>-</td><td nowrap>. . .</td></tr>
	        </table>
	        -->
		</td></tr>
        </table>
		</div>
        <p/>
        
        <h2 class="release">Features</h2>
        <div class="section">
        Click to enlarge:
        <table class="nomargin">
        <tr><td><a href="./assets/rapidfire_screenshot_1.png"><img class="noborder" src="./assets/rapidfire_screenshot_1_preview.png"></a><br>File copy status</td>
        </tr>
        </table>
        <ul>
        <li>Copy data while production is active.</li>
        <li>Reduce downtime when changing large tables.</li>
        </ul>
        </div>     
        
        <h2 class="release">Installation</h2>
        <div class="section">
        The easiest way to install the Rapid Fire plug-in is using the official update sites:
        <p/>
        <table class="nomargin">
        <tr><td><img class="noborder" src="./assets/updatesite.png"></td><td><a href="http://rapid-fire.sourceforge.net/eclipse/rdi8.0/">IBM Rational Developer for i - RDi 9.5.1.3+</a></td></tr>
        <tr><td><img class="noborder" src="./assets/updatesite.png"></td><td><a href="http://rapid-fire.sourceforge.net/eclipse/wdsci7.0/">Websphere Development Studio Client for iSeries - WDSCi 7.0</a></td></tr>
        </table>
        <p/>
        Refer to the Rapid Fire <a target="_help" href="http://rapid-fire.sourceforge.net/help/">help</a> page for detailed installation instructions.
        <p/>
        The Rapid Fire beta versions are available here:
        <p/>
        <table class="nomargin">
        <tr><td><img class="noborder" src="./assets/updatesite_beta.png"></td><td><a href="http://rapid-fire.sourceforge.net/beta-version/eclipse/rdi8.0/">IBM Rational Developer for i - RDi 9.5.1.3+ (beta)</a></td></tr>
        <tr><td><img class="noborder" src="./assets/updatesite_beta.png"></td><td><a href="http://rapid-fire.sourceforge.net/beta-version/eclipse/wdsci7.0/">Websphere Development Studio Client for iSeries - WDSCi 7.0 (beta)</a></td></tr>
        </table>
        <p/>
        Refer to the Rapid Fire <a target="_help" href="http://rapid-fire.sourceforge.net/beta-version/help/">help (beta version)</a> page for detailed installation instructions.
        </div>
        
        <h2 class="release">Help</h2>
        <div class="section">
        <table>
        <tr><td>Visit the Rapid Fire <a target="_help" href="http://rapid-fire.sourceforge.net/help/">help</a> page.</td></tr>
        <tr><td>Ask your questions at the <a target="_wdsci-l" href="http://lists.midrange.com/mailman/listinfo/wdsci-l">WDSCI-L</a> mailing list at <a target="_wdsci-l" href="http://www.midrange.com">midrange.com</a>.</td></tr>
        <tr><td>For bug reports open a ticket at the <a href="https://sourceforge.net/p/rapid-fire/tickets/">Rapid Fire bug tracker.</a></td></tr>
        </table>
        </div>
		
		<h2 class="release">Trademarks</h2>
        <div class="section">
		The following terms are trademarks of the IBM Corporation in the United States or other countries or both:
		<ul>
		<li>Websphere Development Studio Client for iSeries 7.0</li>
		<li>IBM Rational Developer for i 9.5.1.3+</li>
		</ul>
		</div>
        <p/>
		
        <br>
        <hr>
        <table border="0" class="copyright">
        <tr><td class="copyright" align="left" width="50%">Version: <?php echo $current_version; ?> - Copyright: 2017, Rapid Fire project team</td><td class="copyright" align="right" width="50%">Updated: @TODAY@</td></tr>
        </table>
        <br>
    </body>
</html>