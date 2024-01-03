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
            $manifestFileContents = file_get_contents('http://sourceforge.net/p/rapid-fire/code/HEAD/tree/trunk/build/Rapid%20Fire%20Notifier/MANIFEST.MF?format=raw');
            $manifest = parseJarManifest($manifestFileContents);
            $current_version = $manifest['Bundle-Version'];
        ?>
      
        <table width="100%" border="0">
        <tr><td align="left" >
        <a href="https://sourceforge.net/projects/rapid-fire/"><img src="assets/rapidfire.png" alt="Rapid Fire Plug-in" height="79" border="0" style="padding-right: 10px;"/></a>
        </td>
        <td align="left" width="100%" >
        <h1>Rapid Fire Plug-in</h1>
        <p>Hi, this is the support page of the Rapid Fire plug-in for RDi and WDSCi.</p>
        <p>Visit the <a target="_help" href="http://rapid-fire.sourceforge.net/help/">Rapid Fire help</a> site to find out more about the plug-in.</p>
        </td>
        <td>
        
        <table>
        <tr>
        <td align="left">
        <img src="assets/rapidfire_support.png" alt="Rapid Fire Plug-in" border="0" style="padding-right: 10px;"/>
        </td>
        </tr>
        <tr>
        <td valign="bottom" align="right" nowrap>
        <b>Version: <?php echo $current_version; ?></b>
        </td>
        </tr>
        </table>
        
        </td>
        </tr>
        </table>
        
        <h2 class="release">Getting Support</h2>
        <div class="section">
        When you are in doubt whether or not something is wrong with Rapid Fire, please, start with the Rapid Fire help and 
        check for remarks regarding your concerns or ask your questions at the 
        <a target="_wdsci-l" href="http://lists.midrange.com/mailman/listinfo/wdsci-l">WDSCI-L</a> mailing list. In
        case you are sure that you spotted a bug, add a bug report at the 
        <a target="_rapidfire-bugs" href="https://sourceforge.net/p/rapid-fire/tickets/">Rapid Fire bug tracker</a>.
        <p>
        <table>
        <tr><td>Visit the Rapid Fire <a target="_help" href="http://rapid-fire.sourceforge.net/help/">help</a> page.</td></tr>
        <tr><td>Ask your questions at the <a target="_wdsci-l" href="http://lists.midrange.com/mailman/listinfo/wdsci-l">WDSCI-L</a> mailing list at <a target="_wdsci-l" href="http://www.midrange.com">midrange.com</a>.</td></tr>
        <tr><td>For bug reports open a ticket at the <a href="https://sourceforge.net/p/rapid-fire/tickets/">Rapid Fire bug tracker.</a></td></tr>
        </table>
        </div>
        <p/>
		
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