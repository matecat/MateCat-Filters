<?php
/**
 * The template for displaying the header
 *
 * Displays all of the head element and everything up until the "site-content" div.
 *
 * @package WordPress
 * @subpackage Twenty_Fifteen
 * @since Twenty Fifteen 1.0
 */
?><!DOCTYPE html>
<html <?php language_attributes(); ?> class="no-js">
<head>
	<meta charset="<?php bloginfo( 'charset' ); ?>">
	<meta name="viewport" content="width=device-width">
	<link rel="profile" href="http://gmpg.org/xfn/11">
	<link rel="pingback" href="<?php bloginfo( 'pingback_url' ); ?>">
	<!--[if lt IE 9]>
	<script src="<?php echo esc_url( get_template_directory_uri() ); ?>/js/html5.js"></script>
	<![endif]-->
	<script>(function(){document.documentElement.className='js'})();</script>
	<?php wp_head(); ?>
</head>

<body <?php body_class(); ?>>
<div id="page" class="hfeed site">
	<a class="skip-link screen-reader-text" href="#content"><?php _e( 'Skip to content', 'twentyfifteen' ); ?></a>

	<div id="sidebar" class="sidebar">
		<header id="masthead" class="site-header" role="banner">
			<div class="site-branding">
				<?php
					if ( is_front_page() && is_home() ) : ?>
						<h1 class="site-title"><a href="<?php echo esc_url( home_url( '/' ) ); ?>" rel="home"><?php bloginfo( 'name' ); ?></a></h1>
					<?php else : ?>
						<p class="site-title"><a href="<?php echo esc_url( home_url( '/' ) ); ?>" rel="home"><?php bloginfo( 'name' ); ?></a></p>
					<?php endif;

					$description = get_bloginfo( 'description', 'display' );
					if ( $description || is_customize_preview() ) : ?>
						<p class="site-description"><?php echo $description; ?></p>
					<?php endif;
				?>
				<button class="secondary-toggle"><?php _e( 'Menu and widgets', 'twentyfifteen' ); ?></button>
			</div><!-- .site-branding -->
		</header><!-- .site-header -->

		<?php get_sidebar(); ?>
	</div><!-- .sidebar -->
<h1>Sample Files for Demonstrating <br />Duxbury DBT Braille Translator<br />at Exhibit Venues</h1>

<img src="samples/world-flag.jpg" width="250" height="250" alt="a globe made up of national flags">
<img src="samples/bplogo.jpg" width="300" height="250" alt="logo for Braille Planet">
<img src="samples/world-flag.jpg" width="250" height="250" alt="a globe made up of national flags">
</center>
<img src="samples/rainban.gif" alt="ribbon graphic">
<p>The Purpose of this web page (<code><b>http://www.duxburysystems.com/samples.htm</b></code>) is to enhance the 
demonstration of the Duxbury DBT braille translator for different languages.</p>

<p>Imagine you were demonstrating Duxbury DBT at a convention exhibit. 
<br />Someone approaches and asks you to show Duxbury DBT translating Polish text into Polish braille. 
<br />With this web page you can do so in a few seconds.</p>

<p>Some exhibit halls or demonstration venues offer Internet access, some do not. 
<br />This material works with or without Internet access.
<br />If you want to demonstrate these files without Internet access, you need to do preparation
to copy the necessary files onto media you carry to the exhibit area.
<br />You also need to practice so the demonstration can happen smoothly.</p>

<p>Most of the samples have a portion of the Wikipedia entry about growing apples (the fruit, not the company).</p>

<p>This is a work in progress. Any <b>Sample</b> File URL containing <b>zz.doc</b> is not prepared yet.</p>

<img src="samples/rainban.gif" alt="ribbon graphic"><br />
<h2>Learning the Basics of DBT</h2>
<img src="samples/teaching.jpg" width="300" height="300" alt="graphic showing a lesson taking place">

<p>Occasionally someone brand new to Duxbury DBT needs to quickly run a test. They need to answer the question
<b>Does Duxbury DBT produce proper Uzbec Braille?</b> (substitute your favorite language). So they need to 
download DBT (a demo will do), download one of these sample files, set up an embosser, and quickly produce a
sample to see if this technology is going to function properly. The purpose of this section is to try to explain
some of the basic issues so you can accomplish this task.</p>

<p><a href="http://www.duxburysystems.com/product2.asp?product=DBT%20Win&level=major&action=up">
Duxbury DBT can be downloaded from the Duxbury Systems website</a> and used as a demo without a license.
When you use DBT in its demo mode, the braille produced has deliberate flaws.
You need to decide when you to purchase a license so you can produce proper braille.
Please do not get caught up saying "I need to produce perfect braille before I can purchase a license".
You can e-mail a Microsoft Word file to Duxbury Systems and we can e-mail back a Duxbury DBT dxb file,
if that would help you decide if the quality of the braille translator meets your standards.</p>

<p>Duxbury DBT has been localized for about 10 languages. If you have 1-4 weeks to spare, you can help us
localize Duxbury DBT into your language.</p>

<p>For some applications, you want DBT to keep track of different languages in a single file. Microsoft Word embeds <b>language tags</b>
in your file to mark the places where the author switches to a different language. Since all of these files are in a single language,
we do not want to have DBT see any of these language tags (they cannot improve things, and they might interfer).
We recommend that you go to the <b>Global Menu</b>, then select <b>Word Importer</b> and check the bottom checkbox to 
<b>ignore language tags</b>. You can uncheck the box at a later time.</p>

<p>The Duxbury DBT <b>Help Menu</b> has an item called <b>Translate Help</b>. 
<b>Translate Help</b> uses Google Translate to give access to the 
<a href="http://www.duxburysystems.com/documentation/dbt11.2/index.asp?Language=EN&Screenreader=0">DBT Help</a> text.</p>

<p>Duxbury DBT primarily imports Microsoft Word files that use Unicode fonts. For those who do not like to
purchase Microsoft software, there is an alternative from Open Office. If a user has material not encoded in
a Unicode font, it may not work with Duxbury DBT.</p>

<h3>Things You Will Need to Know</h3>

<p>Press <b>Control-O</b> to Open a file within DBT, for the demonstrations we have arranged with this web site,
paste the <b>Sample File URL</b> into the <b>Open File</b> Selection, then Press <b>Enter</b></p>

<p>Duxbury DBT keeps track of whether a file is <b>Print</b> or </b>Braille</b>. When you Open one of the sample files,
make sure you select the <b>Print</b> radio button. 

<p>Duxbury Systems uses a construct called a Template to set the Braille Translation rules that are to be used.
The Template is selected when you open a file. In the chart below, each language has a <b>Sample File URL</b>, 
and also lists the <b>DBT Template</b> that you are to select.</p>

<p>If you do not see the Template you want in the list provided for you, select <b>Region</b> from the center of the
Screen. In the next screen, select <b>All Regions</b>. Now you should be able to find the DBT Template you are
looking for.</p>

<p>For Arabic, Hebrew, and Urdu, the characters are shown left-to-right. Braille is always left to right.
For these languages, you do not want to do any editing in DBT. Instead, do your editing in Microsoft Word.</p>
<p>For Urdu, you may need to install the special font that is referenced in the chart below.</p>
<p>For Korean, the characters are broken down to their individual sounds. Again do your editing in Microsoft Word.</p>
<p>The following scripts may not display within DBT on Windows XP machines: Oriya, Sinhala, Lao, Tibet, Myanmar, Ethiopic, and Khmer.</p>
<p>When it is appropriate in your demonstration, press Control-T to <b>Translate into Braille</b></p>
<p>If you need to, press Control-F6 to switch to a Braille font, or Control-F5 to switch to an ASCII font.</p>
<p>If you are new to Duxbury DBT, please work with DBT, and use the Control-F5 and Control-F6 commands so you
know which font to use (ASCII or Braille) to show Duxbury DBT to your customer.</p>
<hr />
<img src="samples/romeo50.jpg" alt="photograph of a braille embosser">

<h3>Embossers</h3>
<p>The last element in a rushed braille test is getting physical braille. Here we have to say there is no
way to do this quickly. There are so many different variations of computers, operating systems, ports, connecting devices,
embossers, firmware revisions that this is not easy. To get started in a hurry, go to the DBT Global Menu, Embosser Setup.
<p>The single biggest problem in setting up embossers is not changing the prompt <b>Output Options</b> to <b>Send to Printer</b>
instead of <b>Write to Port</b>. The setting <b>Write to Port</b> is useful on older computers.
The link below on USB connections is especially valuable.</p>

<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/embossers_and_embossing/usb_and_embossers.htm">USB Ports and Embossers</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/the_menus/MENU_GLOBAL/global_embosser_setup.htm">Global Embosser Setup</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/the_menus/MENU_GLOBAL/Global_Embosser_setup_general.htm">General screen</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/the_menus/MENU_GLOBAL/Global_Embosser_setup_settings.htm">Device Settings screen</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/the_menus/MENU_GLOBAL/Global_Embosser_setup_advanced.htm">Advanced Settings screen</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/embossers_and_embossing/Embosser_Manufacturers.htm">List of Brailler Manufacturers</a>
<br /><a href="http://www.duxburysystems.com/documentation/dbt11.2/troubleshooting/Windows_Printer_Drivers.htm">Windows Printer Drivers</a>
<br /><a href="http://www.duxburysystems.com/super_gem.htm">Discussion about the Super Gemini from Nippon Telesoft</a>
</p>


<img src="samples/rainban.gif" alt="ribbon graphic"><br />
<h2>Demo with Internet Access</h2>
<img src="samples/yes-internet.jpg" width="300" height="300" alt="graphic representing having internet access">
<ul>
<li>Launch Duxbury DBT (current version)</li>
<li>Launch your browser, open this file: <code><b>http://www.duxburysystems.com/samples.htm</b></code></li>
<li>Find the correct <b>Language</b> in these tables on this web page</li>
<li>Do <b>not</b> click on the <b>Sample File URL</b></li>
<li>Instead, Copy the <b>Sample File URL</b> into the Clipboard.</li>
<li>Switch to the Duxbury DBT Application</li>
<li>Press <b>Control-O</b> to Open a file within DBT.</li>
<li>Paste the <b>Sample File URL</b> into the <b>Open File</b> Selection, then Press <b>Enter</b></li>
<li>When Prompted, select the correct <b>DBT Template</b> (see last column on this web page)</li>
<li>When it is appropriate in your demonstration, press Control-T to <b>Translate into Braille</b></li>
</ul>

<h3>To Demo the File within Microsoft Word</h3>
<ul>
<li>Click on the <b>Sample File URL</b> in this web page (on-line or off-line)</li>
<li>Microsoft Word opens with the correct sample file.</li>
<li>When you need to, show the same file in Duxbury DBT.</li>
</ul>

<img src="samples/rainban.gif" alt="ribbon graphic"><br />
<h2>Demo with No Internet Access</h2>
<img src="samples/no-internet.jpg" width="300" height="250" alt="graphic representing not having internet access">
<h3>Preparation for the Demo</h3>
<ul>
<li>Obtain the <a href="http://www.duxburysystems.com/samples.zip">samples.zip</a> file from Duxbury Systems</li>
<li>Unzip this file onto your computer's hard drive or to media you will take to the exhibition venue.</li>
<li>Make sure that you install the directory at <code>[drive letter]:\</code></li>
<li>Test that you can open the HTML file <code>[drive letter]\dbt_samples\samp_[drive letter].htm</code> in your browser.</li>
</ul>

<h3>Demo with No Internet Access</h3>
<ul>
<li>Launch Duxbury DBT (current version)</li>
<li>Launch your browser,</li>
<li>Type <b>control-O</b> to open the file, <code>[drive letter]\dbt_samples\samp_[drive letter].htm</code> on your portable media</li>
<li>Find the correct Language in these tables on this web page</li>
<li>Do <b>not</b> click on the <b>Sample File URL</b></li>
<li>Instead, Copy the <b>Sample File URL</b> into the Clipboard.</li>
<li>Switch to the Duxbury DBT Application</li>
<li>Press <b>Control-O</b> to Open a file within DBT.</li>
<li>Paste the <b>Sample File URL</b> into the <b>Open File</b> Selection, then Press <b>Enter</b></li>
<li>When Prompted, select the correct <b>DBT Template</b> (see last column on this web page)</li>
<li>When it is appropriate in your demonstration, press Control-T to <b>Translate into Braille</b></li>
</ul>

<h3>To Demo the File within Microsoft Word</h3>
<ul>
<li>Click on the <b>Sample File URL</b> in this web page (on-line or off-line)</li>
<li>Microsoft Word opens with the correct sample file.</li>
<li>When you need to, show the same file in Duxbury DBT.</li>
</ul>

<img src="samples/rainban.gif" alt="ribbon graphic">
<h1>Sample Microsoft Word files</h1>
<p>This might be the biggest collection of electronic files on the internet showing samples of different languages
 (which does not rely on graphics to show difficult scripts). All of these files are encoded in Unicode.</p>
<h2>Europe and Western Hemisphere</h2>
<table class=lead border="2" cellspacing="6">
<tr><th>Flags</th><th>Language</th><th>Sample File URL</th><th>DBT Template Name</th></tr>
<tr><td><img src="flags/albania.gif" alt="Albanian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Albanian.asp">Albanian</a></td><td><a href="samples/albanian-sq.doc">http://www.duxburysystems.com/samples/albanian-sq.doc</a></td><td>Albanian</td></tr>
<tr><td><img src="flags/belarus.gif" alt="Flag of Belarus" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Belarusian.asp">Belarusian</a></td><td><a href="samples/belarusian-be.doc">http://www.duxburysystems.com/samples/belarusian-be.doc</a></td><td>Belarusian</td></tr>
<tr><td><img src="flags/bosnia.gif" alt="Flag of Bosnia and Herzegovina" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Bosnian.asp">Bosnian</a></td><td><a href="samples/bosnian-bs.doc">http://www.duxburysystems.com/samples/bosnian-bs.doc</a></td><td>Bosnian</td></tr>
<tr><td><img src="flags/bulgaria.gif" alt="Flag of Bulgaria" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Bulgarian.asp">Bulgarian</a></td><td><a href="samples/bulgarian-bg.doc">http://www.duxburysystems.com/samples/bulgarian-bg.doc</a></td><td>Bulgarian</td></tr>
<tr><td><img src="flags/andorra.gif" alt="Flag of Andora" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Catalan.asp">Catalan</a></td><td><a href="samples/catalan-ca.doc">http://www.duxburysystems.com/samples/catalan-ca.doc</a></td><td>Catalan</td></tr>
<tr><td><img src="flags/croatia.gif" alt="Flag of Croatia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Croatian.asp">Croatian</a></td><td><a href="samples/croatian-hr.doc">http://www.duxburysystems.com/samples/croatian-hr.doc</a></td><td>Croatian</td></tr> 
<tr><td><img src="flags/czech.gif" alt="Flag of Czech Republic" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Czech.asp">Czech</a></td><td><a href="samples/czech-cs.doc">http://www.duxburysystems.com/samples/czech-cs.doc</a></td><td>Czech</td></tr>
<tr><td><img src="flags/dk.gif" alt="Flag of Denmark" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Danish.asp">Danish</a></td><td><a href="samples/danish-da.doc">http://www.duxburysystems.com/samples/danish-da.doc</a></td><td>Danish</td></tr>
<tr><td><img src="flags/netherlands.gif" alt="Flag of Netherlands" width="40" height="24" border="1"><img src="flags/belgium.gif" alt="Flag of Belgium" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Dutch.asp">Dutch</a></td><td><a href="samples/dutch-nl.doc">http://www.duxburysystems.com/samples/dutch-nl.doc</a></td><td>Dutch</td></tr>
<tr><td><img src="flags/canada.gif" alt="Flag of Canada" width="40" height="24" border="1"><img src="flags/united kingdom.gif" alt="Flag of the United Kingdom" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_English_unified.asp">English</a></td><td><a href="samples/english-en.doc">http://www.duxburysystems.com/samples/english-en.doc</a></td><td>English (Unified)</td></tr>
<tr><td><img src="flags/united states.gif" alt="Flag of the United States" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_English_american_textbook.asp">English</a></td><td><a href="samples/english-en.doc">http://www.duxburysystems.com/samples/english-en.doc</a></td><td>English (American Textbook DE) - Textbook format</td></tr>
<tr><td><img src="flags/estonia.gif" alt="Flag of Estonian  " width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Estonian.asp">Estonian</a></td><td><a href="samples/estonian-et.doc">http://www.duxburysystems.com/samples/estonian-et.doc</a></td><td>Estonian</td></tr>
<tr><td><img src="flags/finland.gif" alt="Flag of Finland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Finnish.asp">Finnish</a></td><td><a href="samples/finnish-fi.doc">http://www.duxburysystems.com/samples/finnish-fi.doc</a></td><td>Finnish</td></tr>
<tr><td><img src="flags/france.gif" alt="Flag of France" width="40" height="24" border="1"><img src="flags/belgium.gif" alt="Flag of Belgium" width="40" height="24" border="1"><img src="flags/switzerland.gif" alt="Flag of Switzerland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_French_unified.asp">French</a></td><td><a href="samples/french-fr.doc">http://www.duxburysystems.com/samples/french-fr.doc</a></td><td>Francais 2006 - abrege</td></tr>
<tr><td><img src="flags/germany.gif" alt="Flag of Germany" width="40" height="24" border="1"><img src="flags/austria.gif" alt="Flag of Austria" width="40" height="24" border="1"><img src="flags/switzerland.gif" alt="Flag of Switzerland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_german.asp">German</a></td><td><a href="samples/german-de.doc">http://www.duxburysystems.com/samples/german-de.doc</a></td><td>German - basic</td></tr> 
<tr><td><img src="flags/greece.gif" alt="Flag of Greece" width="40" height="24" border="1"><img src="flags/cyprus.gif" alt="Flag of Cyprus" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Greek_modern.asp">Greek</a></td><td><a href="samples/greek-el.doc">http://www.duxburysystems.com/samples/greek-el.doc</a></td><td>Greek (Modern)</td></tr> 
<tr><td><img src="flags/hungary.gif" alt="Flag of Hungary" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Hungarian.asp">Hungarian</a></td><td><a href="samples/hungarian-hu.doc">http://www.duxburysystems.com/samples/hungarian-hu.doc</a></td><td>Hungarian</td></tr>
<tr><td><img src="flags/iceland.gif" alt="Flag of Iceland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Icelandic.asp">Icelandic</a></td><td><a href="samples/icelandic-is.doc">http://www.duxburysystems.com/samples/icelandic-is.doc</a></td><td>Icelandic</td></tr>
<tr><td><img src="flags/united kingdom.gif" alt="Flag of the United Kingdom" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Irish.asp">Irish Gaelic</a></td><td><a href="samples/irish-ga.doc">http://www.duxburysystems.com/samples/irish-ga.doc</a></td><td>Irish</td></tr>
<tr><td><img src="flags/italy.gif" alt="Flag of Italy" width="40" height="24" border="1"><img src="flags/switzerland.gif" alt="Flag of Switzerland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Italian.asp">Italian</a></td><td><a href="samples/italian-it.doc">http://www.duxburysystems.com/samples/italian-it.doc</a></td><td>Italiano</td></tr>
<tr><td><img src="flags/latvia.gif" alt="Flag of Latvia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Latvian.asp">Latvian</a></td><td><a href="samples/latvian-lv.doc">http://www.duxburysystems.com/samples/latvian-lv.doc</a></td><td>Latvian</td></tr>
<tr><td><img src="flags/lithuania.gif" alt="Flag of Lithuania" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_lithuanian.asp">Lithuanian</a></td><td><a href="samples/lithuanian-lt.doc">http://www.duxburysystems.com/samples/lithuanian-lt.doc</a></td><td>Lithuanian</td></tr>
<tr><td><img src="flags/luxembourg.gif" alt="Flag of Luxembourg" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Luxembourgish.asp">Luxembourgish</a></td><td><a href="samples/luxembourgish-lb.doc">http://www.duxburysystems.com/samples/luxembourgish-lb.doc</a></td><td>Luxembourgish</td></tr>
<tr><td><img src="flags/macedonia.gif" alt="Flag of Macedonia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Macedonian.asp">Macedonian</a></td><td><a href="samples/macedonian-mk.doc">http://www.duxburysystems.com/samples/macedonian-mk.doc</a></td><td>Macedonian</td></tr>
<tr><td><img src="flags/malta.gif" alt="Flag of Malta" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Maltese.asp">Maltese</a></td><td><a href="samples/Maltese-mt.doc">http://www.duxburysystems.com/samples/Maltese-mt.doc</a></td><td>Maltese</td></tr>
<tr><td><img src="flags/norway.gif" alt="Flag of Norway" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Norwegian.asp">Norwegian Bokmål </a></td><td><a href="samples/norwegian-nb.doc">http://www.duxburysystems.com/samples/norwegian-nb.doc</a></td><td>Norse</td></tr>
<tr><td><img src="flags/norway.gif" alt="Flag of Norway" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Norwegian.asp">Norwegian Nynorsk</a></td><td><a href="samples/norwegian-nn.doc">http://www.duxburysystems.com/samples/norwegian-nn.doc</a></td><td>Norse</td></tr>
<tr><td><img src="flags/poland.gif" alt="Flag of Poland" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Polish.asp">Polish</a></td><td><a href="samples/polish-pl.doc">http://www.duxburysystems.com/samples/polish-pl.doc</a></td><td>Polish</td></tr>
<tr><td><img src="flags/portugal.gif" alt="Flag of Portugal" width="40" height="24" border="1"><img src="flags/brazil.gif" alt="Flag of Brazil" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Portuguese_brazilian.asp">Portuguese</a></td><td><a href="samples/portuguese-pt.doc">http://www.duxburysystems.com/samples/portuguese-pt.doc</a></td><td>Portuguese Uncontracted</td></tr>
<tr><td><img src="flags/romania.gif" alt="Flag of Romania" width="40" height="24" border="1"><img src="flags/moldovia.gif" alt="Flag of Moldovia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Romanian.asp">Romanian</a></td><td><a href="samples/romanian-ro.doc">http://www.duxburysystems.com/samples/romanian-ro.doc</a></td><td>Romanian</td></tr>
<tr><td><img src="flags/russia.gif" alt="Flag of Russia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Russian.asp">Russian</a></td><td><a href="samples/russian-ru.docx">http://www.duxburysystems.com/samples/russian-ru.docx</a></td><td>Russian - no capitals</td></tr>
<tr><td><img src="flags/serbia.gif" alt="Flag of Serbia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Serbian.asp">Serbian</a></td><td><a href="samples/serbian-sr.doc">http://www.duxburysystems.com/samples/serbian-sr.doc</a></td><td>Serbian</td></tr>
<tr><td><img src="flags/slovakia.gif" alt="Flag of Slovak Republic" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Slovak.asp">Slovak</a></td><td><a href="samples/slovak-sk.doc">http://www.duxburysystems.com/samples/slovak-sk.doc</a></td><td>Slovakian</td></tr>
<tr><td><img src="flags/slovenia.gif" alt="Flag of Slovenia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Slovenian.asp">Slovenian</a></td><td><a href="samples/slovenian-sl.doc">http://www.duxburysystems.com/samples/slovenian-sl.doc</a></td><td>Slovenian</td></tr>
<tr><td><img src="flags/spain.gif" alt="Flag of Spain" width="40" height="24" border="1"><img src="flags/mexico.gif" alt="Flag of Mexico" width="40" height="24" border="1"><img src="flags/argentina.gif" alt="Flag of Argentina" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Spanish.asp">Spanish</a></td><td><a href="samples/spanish-es.docx">http://www.duxburysystems.com/samples/spanish-es.docx</a></td><td>Espanol sin Contracciones</td></tr>
<tr><td><img src="flags/sweden.gif" alt="Flag of Sweden" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Swedish.asp">Swedish</a></td><td><a href="samples/swedish-sv.doc">http://www.duxburysystems.com/samples/swedish-sv.doc</a></td><td>Swedish Uncontracted</td></tr>
<tr><td><img src="flags/turkey.gif" alt="Turkey Flag" width="40" height="24" border="1"><img src="flags/cyprus.gif" alt="Flag of Cyprus" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Turkish.asp">Turkish</a></td><td><a href="samples/turkish-tr.doc">http://www.duxburysystems.com/samples/turkish-tr.doc</a></td><td>Turkish</td></tr>
<tr><td><img src="flags/ukraine.gif" alt="Flag of Ukraine" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Ukrainian.asp">Ukrainian</a></td><td><a href="samples/ukrainian-uk.doc">http://www.duxburysystems.com/samples/ukrainian-uk.doc</a></td><td>Ukrainian</td></tr>
</table>
<p><hr /></p>

<h2>Asia</h2>
<table class=lead border="2" cellspacing="6">
<tr><th>Flags</th><th>Language</th><th>Sample File URL</th><th>DBT Template Name</th><th>Comments</th></tr></tr>
<tr><td><img src="flags/egypt.gif" alt="Egyptian Flag" width="40" height="24" border="1"><img src="flags/united arab emirates.gif" alt="United Arab Emirates Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Arabic.asp">Arabic</a></td><td><a href="samples/arabic-ar.docx">http://www.duxburysystems.com/samples/arabic-ar.docx</a></td><td>Arabic</td></tr> 
<tr><td><img src="flags/armenia.gif" alt="Armenian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Armenian_eastern.asp">Armenian, Eastern</a></td><td><a href="samples/armenian-hy.docx">http://www.duxburysystems.com/samples/armenian-hy.docx</a></td><td>Armenian, Eastern</td></tr> 
<tr><td><img src="flags/armenia.gif" alt="Armenian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Armenian_western.asp">Armenian, Western</a></td><td><a href="samples/armenian-hy.docx">http://www.duxburysystems.com/samples/armenian-hy.docx</a></td><td>Armenian, Western</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Assamese.asp">Assamese</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Assanese</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Awadhi.asp">Awadhi</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Awadhi</td></tr>  
<tr><td><img src="flags/azerbai.gif" alt="Azerbaijan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Azerbaijani.asp">Azerbaijani</a></td><td><a href="samples/azeri-az.docx">http://www.duxburysystems.com/samples/azeri-az.docx</a></td><td>Azerjaijani</td></tr> 
<tr><td><img src="flags/bangladesh.gif" alt="Bangladesh Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Bengali_bangladesh.asp">Bengali (Bangla) for Bangladesh</a></td><td><a href="samples/bengali-bn.docx">http://www.duxburysystems.com/samples/bengali-bn.docx</a></td><td>Bengali (Bangla) for Bangladesh</td></tr>  
<tr><td><img src="flags/india.gif" alt="India Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Bengali_india.asp">Bengali (Bangla) for India</a></td><td><a href="samples/bengali-bn.docx">http://www.duxburysystems.com/samples/bengali-bn.docx</a></td><td>Bengali (Bangla) for India</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_bhojpuri.asp">Bhojpuri</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Bjojpuri</td></tr>  
<tr><td><img src="flags/myanmar.gif" alt="Myanmar Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Burmese.asp">Burmese</a></td><td><a href="samples/burmese_my.docx">http://www.duxburysystems.com/samples/burmese_my.docx</a></td><td>Burmese</td></tr> 
<tr><td><img src="flags/philippines.gif" alt="Philippines Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Cebuano.asp">Cebuano</a></td><td><a href="samples/cebuano.docx">http://www.duxburysystems.com/samples/cebuano.docx</a></td><td>Cebuano</td></tr> 
<tr><td><img src="flags/china.gif" alt="Chinese Flag" width="40" height="24" border="1"><img src="flags/singapore.gif" alt="Singapore Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Chinese_Mandarin.asp">Chinese Mandarin</a></td><td><a href="samples/chinese-zh.doc">http://www.duxburysystems.com/samples/chinese-zh.doc</a></td><td>Chinese/Mandarin</td></tr> 
<tr><td><img src="flags/china.gif" alt="Chinese Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Chinese_Yue.asp">Chinese/Yue (Cantonese)</a></td><td><a href="samples/cantonese.docx">http://www.duxburysystems.com/samples/cantonese.docx</a></td><td>Chinese Yue (Cantonese)</td></tr> 
<tr><td><img src="flags/bangladesh.gif" alt="Bangladesh Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Chittagonian.asp">Chittagonian</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Chittagonian</td></tr>  
<tr><td><img src="flags/af-flag.gif" alt="Afghanistan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Dari.asp">Dari</a></td><td><a href="samples/farsi-fa.doc">http://www.duxburysystems.com/samples/farsi-fa.doc</a></td><td>Dari</td></tr> 
<tr><td><img src="flags/bhutan.gif" alt="Bhutan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Dzongkha.asp">Dzongkha</a></td><td><a href="samples/dzongkha-dz.docx">http://www.duxburysystems.com/samples/dzongkha-dz.docx</a></td><td>Dzongkha</td></tr> 
<tr><td><img src="flags/australia.gif" alt="Flag of Australia" width="40" height="24" border="1"><img src="flags/new zealand.gif" alt="Flag of New Zealand" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_English_unified.asp">English</a></td><td><a href="samples/english-en.doc">http://www.duxburysystems.com/samples/english-en.doc</a></td><td>English (Unified)</td></tr>
<tr><td><img src="flags/iran.gif" alt="Iranian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Farsi.asp">Farsi (Persian)</a></td><td><a href="samples/farsi-fa.doc">http://www.duxburysystems.com/samples/farsi-fa.doc</a></td><td>Farsi (Persian)</td></tr> 
<tr><td><img src="flags/philippines.gif" alt="Philippines Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Filipino.asp">Filipino</a></td><td><a href="samples/filipino-fil.doc">http://www.duxburysystems.com/samples/filipino-fil.doc</a></td><td>Filipino</td></tr> 
<tr><td><img src="flags/georgia.gif" alt="Georgian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Georgian.asp">Georgian</a></td><td><a href="samples/georgian-ka.docx">http://www.duxburysystems.com/samples/georgian-ka.docx</a></td><td>Georgian</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Gujurati.asp">Gujurati</a></td><td><a href="samples/gujurati_gu.docx">http://www.duxburysystems.com/samples/gujurati_gu.docx</a></td><td>Gujurati</td></tr>
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Haryanvi.asp">Haryanvi</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Haryanvi</td></tr>  
<tr><td><img src="flags/israel.gif" alt="Israeli Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Hebrew_israeli.asp">Hebrew/Israeli</a></td><td><a href="samples/hebrew-he.doc">http://www.duxburysystems.com/samples/hebrew-he.doc</a></td><td>Hebrew (Israeli)</td></tr> 
<tr><td><img src="flags/philippines.gif" alt="Philippines Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Hiligaynon.asp">Hiligaynon</a></td><td><a href="samples/hiligaynon.docx">http://www.duxburysystems.com/samples/hiligaynon.docx</a></td><td>Hiligaynon</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Hindi.asp">Hindi</a></td><td><a href="samples/hindi-hi.docx">http://www.duxburysystems.com/samples/hindi-hi.docx</a></td><td>Hindi</td></tr> 
<tr><td><img src="flags/philippines.gif" alt="Philippines Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Iloko.asp">Iloko</a></td><td><a href="samples/iloko.docx">http://www.duxburysystems.com/samples/iloko.docx</a></td><td>Iloko</td></tr> 
<tr><td><img src="flags/indonesia.gif" alt="Indonesia Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Indonesian.asp">Indonesian</a></td><td><a href="samples/indonesian-id.doc">http://www.duxburysystems.com/samples/indonesian-id.doc</a></td><td>Indonesian</td></tr> 
<tr><td><img src="flags/japan.gif" alt="Japanese Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Japanese_kana.asp">Japanese (Kana)</a></td><td><a href="samples/japanese-ja.docx">http://www.duxburysystems.com/samples/japanese-ja.docx</a></td><td>Japanese (Kana)</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Kannada.asp">Kannada</a></td><td><a href="samples/kannada_kn.docx">http://www.duxburysystems.com/samples/kannada_kn.docx</a></td><td>Kannada</td></tr>  
<tr><td><img src="flags/kazakhstan.gif" alt="kazakistan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Kazakh.asp">Kazakh</a></td><td><a href="samples/kazakh-kk.docx">http://www.duxburysystems.com/samples/kazakh-kk.docx</a></td><td>Kazakh</td></tr> 
<tr><td><img src="flags/cambodia.gif" alt="Cambodian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Khmer.asp">Khmer (Cambodian)</a></td><td><a href="samples/khmer-km.docx">http://www.duxburysystems.com/samples/khmer-km.docx</a></td><td>Khmer</td></tr> 
<tr><td><img src="flags/kyrgyzstan.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Kirghiz.asp">Kirghiz</a></td><td><a href="samples/kirghiz-ky.docx">http://www.duxburysystems.com/samples/kirghiz-ky.docx</a></td><td>Kirghiz</td></tr> 
<tr><td><img src="flags/southkorea.gif" alt="South Korean Flag" width="40" height="24" border="1"><img src="flags/northkorea.gif" alt="North Korean Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Korean.asp">Korean</a></td><td><a href="samples/korean-ko.docx">http://www.duxburysystems.com/samples/korean-ko.docx</a></td><td>Korean</td></tr> 
<tr><td><img src="flags/iraq.gif" alt="Iraq Flag" width="40" height="24" border="1"><img src="flags/turkey.gif" alt="Turkey Flag" width="40" height="24" border="1"><img src="flags/iran.gif" alt="Iran Flag" width="40" height="24" border="1"><img src="flags/kurdish.gif" alt="Kurdish Flag" width="40" height="24" border="1"></td></td><td><a href="http://www.duxburysystems.com/lan_Kurdish.asp">Kurdish</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Kurdish</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Kurukh.asp">Kurukh</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Kurukh</td></tr>  
<tr><td><img src="flags/laos.gif" alt="Lao Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Lao.asp">Lao</a></td><td><a href="samples/lao-lo.docx">http://www.duxburysystems.com/samples/lao-lo.docx</a></td><td>Lao</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Magahi.asp">Magahi</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Magahi</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Maithili.asp">Maithili</a></td><td><a href="samples/maithili.docx">http://www.duxburysystems.com/samples/maithili.docx</a></td><td>Maithili</td></tr>  
<tr><td><img src="flags/malaysia.gif" alt="Malaysia Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Malay.asp">Malay</a></td><td><a href="samples/malay-ms.doc">http://www.duxburysystems.com/samples/malay-ms.doc</a></td><td>Malay</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Malayalam.asp">Malayalam</a></td><td><a href="samples/malayalam_ml.docx">http://www.duxburysystems.com/samples/malayalam_ml.docx</a></td><td>Malayalam</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Marathi.asp">Marathi</a></td><td><a href="samples/marathi_mr.docx">http://www.duxburysystems.com/samples/marathi_mr.docx</a></td><td>Marathi</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Meitei.asp">Meitei (Manipuri)</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Meitei (Manipuri)</td></tr>  
<tr><td><img src="flags/mongolia.gif" alt="Mongolian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Mongolian.asp">Mongolian</a></td><td><a href="samples/mongolian-mn.docx">http://www.duxburysystems.com/samples/mongolian-mn.docx</a></td><td>Mongolian</td></tr> 
<tr><td><img src="flags/nepal.gif" alt="Nepal Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Nepali.asp">Nepali</a></td><td><a href="samples/nepali-ne.docx">http://www.duxburysystems.com/samples/nepali-ne.docx</a></td><td>Nepali</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Oriya.asp">Oriya</a></td><td><a href="samples/oriya_or.docx">http://www.duxburysystems.com/samples/oriya_or.docx</a></td><td>Oriya</td></tr>  
<tr><td><img src="flags/pakistan.gif" alt="Pakistan Flag" width="40" height="24" border="1"><img src="flags/india.gif" alt="India Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Panjabi.asp">Panjabi</a></td><td><a href="samples/panjabi_pa.docx">http://www.duxburysystems.com/samples/panjabi_pa.docx</a></td><td>Panjabi</td></tr> 
<tr><td><img src="flags/pakistan.gif" alt="Pakistan Flag" width="40" height="24" border="1"><img src="flags/af-flag.gif" alt="Afghanistan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Pushto.asp">Pashto</a></td><td><a href="samples/pashto-ps.docx">http://www.duxburysystems.com/samples/pashto-ps.docx</a></td><td>Pashto</td></tr> 
<tr><td><img src="flags/russia.gif" alt="Flag of Russia" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Russian.asp">Russian</a></td><td><a href="samples/russian-ru.docx">http://www.duxburysystems.com/samples/russian-ru.docx</a></td><td>Russian</td></tr>
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sadri.asp">Sadri</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Sadri</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sanskrit.asp">Sanskrit</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Sanskrit</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Santali.asp">Santali</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Santali</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sindhi.asp">Sindhi</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Sindi</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sinhala.asp">Sinhala</a></td><td><a href="samples/sinhala_si.docx">http://www.duxburysystems.com/samples/sinhala_si.docx</a></td><td>Sinhala</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sylheti.asp">Sylheti</a></td><td><a href="samples/zz.doc">http://www.duxburysystems.com/samples/zz.doc</a></td><td>Sylheti</td></tr>  
<tr><td><img src="flags/philippines.gif" alt="Philippines Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tagalog.asp">Tagalog</a></td><td><a href="samples/tagalog-tl.doc">http://www.duxburysystems.com/samples/tagalog-tl.doc</a></td><td>Tagalog</td></tr> 
<tr><td><img src="flags/tajikistan.gif" alt="Tajik Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tajik.asp">Tajik</a></td><td><a href="samples/tajik-tj.docx">http://www.duxburysystems.com/samples/tajik-tj.docx</a></td><td>Tajik</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"><img src="flags/srilanka.gif" alt="Sri Lankan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tamil.asp">Tamil</a></td><td><a href="samples/tamil_ta.docx">http://www.duxburysystems.com/samples/tamil_ta.docx</a></td><td>Tamil</td></tr>  
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Telugu.asp">Telugu</a></td><td><a href="samples/telugu_te.docx">http://www.duxburysystems.com/samples/telugu_te.docx</a></td><td>Telugu</td></tr>  
<tr><td><img src="flags/thailand.gif" alt="Thai Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Thai.asp">Thai</a></td><td><a href="samples/thai-th.doc">http://www.duxburysystems.com/samples/thai-th.doc</a></td><td>Thai</td></tr> 
<tr><td><img src="flags/china.gif" alt="Chinese Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tibetan.asp">Tibetan</a></td><td><a href="samples/tibet-bo.docx">http://www.duxburysystems.com/samples/tibet-bo.docx</a></td><td>Tibetan</td></tr> 
<tr><td><img src="flags/turkey.gif" alt="Turkey Flag" width="40" height="24" border="1"><img src="flags/cyprus.gif" alt="Flag of Cyprus" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Turkish.asp">Turkish</a></td><td><a href="samples/turkish-tr.doc">http://www.duxburysystems.com/samples/turkish-tr.doc</a></td><td>Turkish</td></tr>
<tr><td><img src="flags/turkmen.gif" alt="Turkmen Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Turkmen.asp">Turkmen</a></td><td><a href="samples/turkmen-tk.docx">http://www.duxburysystems.com/samples/turkmen-tk.docx</a></td><td>Turkmen</td></tr> 
<tr><td><img src="flags/india.gif" alt="Indian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Urdu_India.asp">Urdu/India</a></td><td><a href="samples/urdu-ur.doc">http://www.duxburysystems.com/samples/urdu-ur.doc</a></td><td>Urdu (Indian)</td><td><a href="http://www.bbc.co.uk/urdu/fontinstall/popupwin.shtml">Urdu font</a></td></tr>  
<tr><td><img src="flags/pakistan.gif" alt="Pakistan Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Urdu_Pakistan.asp">Urdu/Pakistan</a></td><td><a href="samples/urdu-ur.doc">http://www.duxburysystems.com/samples/urdu-ur.doc</a></td><td>Urdu (Pakistani)</td><td><a href="http://www.bbc.co.uk/urdu/fontinstall/popupwin.shtml">Urdu font</a></td></tr>  
<tr><td><img src="flags/uzbekistan.gif" alt="Uzbek Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Uzbek.asp">Uzbek</a></td><td><a href="samples/uzbek-uz.docx">http://www.duxburysystems.com/samples/uzbek-uz.docx</a></td><td>Uzbek</td></tr> 
<tr><td><img src="flags/vietnam.gif" alt="Vietnam Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Vietnamese.asp">Vietnamese</a></td><td><a href="samples/vietnamese-vi.doc">http://www.duxburysystems.com/samples/vietnamese-vi.doc</a></td><td>Vietnamese</td></tr> 
</table>
<p><hr /></p>

<h2>Africa</h2>
<table class=lead border="2" cellspacing="6">
<tr><th>Flags</th><th>Language</th><th>Sample File URL</th><th>DBT Template Name</th></tr>
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Afrikaans.asp">Afrikaans</a></td><td><a href="samples/afrikaans-af.doc">http://www.duxburysystems.com/samples/afrikaans-af.doc</a></td><td>Afrikaans</td></tr> 
<tr><td><img src="flags/ethiopia.gif" alt="Ethiopia Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Amharic.asp">Amharic</a></td><td><a href="samples/amharic-am.doc">http://www.duxburysystems.com/samples/amharic-am.doc</a></td><td>Amharic</td></tr> 
<tr><td><img src="flags/egypt.gif" alt="Egyptian Flag" width="40" height="24" border="1"><img src="flags/united arab emirates.gif" alt="United Arab Emirates Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Arabic.asp">Arabic</a></td><td><a href="samples/arabic-ar.docx">http://www.duxburysystems.com/samples/arabic-ar.docx</a></td><td>Arabic</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_English_unified.asp">English</a></td><td><a href="samples/english-en.doc">http://www.duxburysystems.com/samples/english-en.doc</a></td><td>English (Unified)</td></tr>
<tr><td><img src="flags/ghana.gif" alt="Ghana Flag" width="40" height="24" border="1"><img src="flags/togo.gif" alt="Togo Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Ewe.asp">&Eacute;w&eacute;</a></td><td><a href="samples/ewe.docx">http://www.duxburysystems.com/samples/ewe.docx</a></td><td>Ewe</td></tr> 
<tr><td><img src="flags/ni-flag.gif" alt="Nigeria Flag" width="40" height="24" border="1"><img src="flags/ng-flag.gif" alt="Flag of Niger" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Hausa.asp">Hausa</a></td><td><a href="samples/hausa-ha.doc">http://www.duxburysystems.com/samples/hausa-ha.doc</a></td><td>Hausa</td></tr>  
<tr><td><img src="flags/ni-flag.gif" alt="Nigeria Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Igbo.asp">Igbo</a></td><td><a href="samples/igbo-ig.doc">http://www.duxburysystems.com/samples/igbo-ig.doc</a></td><td>Igbo</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"><img src="flags/zimbabwe.gif" alt="Zimbabwe Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Ndebele.asp">Ndebele</a></td><td><a href="samples/ndebele-nr.docx">http://www.duxburysystems.com/samples/ndebele-nr.docx</a></td><td>Ndebele</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Nguni.asp">Nguni</a></td><td><a href="samples/nguni.docx">http://www.duxburysystems.com/samples/nguni.docx</a></td><td>Nguni (Xhosa/Zulu)</td></tr> 
<tr><td><img src="flags/somalia.gif" alt="Somalia Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_somali.asp">Somali</a></td><td><a href="samples/somali-so.doc">http://www.duxburysystems.com/samples/somali-so.doc</a></td><td>Somali</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Sotho.asp">Sotho (Southern or Northern (Pedi))</a></td><td><a href="samples/sotho.docx">http://www.duxburysystems.com/samples/sotho.docx</a></td><td>Sotho (Southern or Northern (Pedi))</td></tr> 
<tr><td><img src="flags/kenya.gif" alt="Kenya Flag" width="40" height="24" border="1"><img src="flags/tanzania.gif" alt="Tanzania Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Swahili.asp">Swahili</a></td><td><a href="samples/swahili-sw.doc">http://www.duxburysystems.com/samples/swahili-sw.doc</a></td><td>Swahili</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"><img src="flags/swaziland.gif" alt="Swaziland Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Swati.asp">Swati</a></td><td><a href="samples/swati.docx">http://www.duxburysystems.com/samples/swati.docx</a></td><td>Swati</td></tr> 
<tr><td><img src="flags/ethiopia.gif" alt="Ethiopian Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tigrinya.asp">Tigrinya</a></td><td><a href="samples/tigrinya-ti.docx">http://www.duxburysystems.com/samples/tigrinya.docx</a></td><td>Tigrinya</td></tr>
<tr><td><img src="flags/mozambique.gif" alt="Mozambique Flag" width="40" height="24" border="1"><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"><img src="flags/swaziland.gif" alt="Swaziland Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tsonga.asp">Tsonga</a></td><td><a href="samples/tsonga.docx">http://www.duxburysystems.com/samples/tsonga.docx</a></td><td>Tsonga</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Tswana.asp">Tswana</a></td><td><a href="samples/tswana.docx">http://www.duxburysystems.com/samples/tswana.docx</a></td><td>Tswana</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Venda.asp">Venda</a></td><td><a href="samples/venda.docx">http://www.duxburysystems.com/samples/venda.docx</a></td><td>Venda</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Xhosa.asp">Xhosa</a></td><td><a href="samples/xhosa-xh.docx">http://www.duxburysystems.com/samples/xhosa-xh.docx</a></td><td>Xhosa</td></tr> 
<tr><td><img src="flags/ni-flag.gif" alt="Nigeria Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Yoruba.asp">Yoruba</a></td><td><a href="samples/yoruba-yo.doc">http://www.duxburysystems.com/samples/yoruba-yo.doc</a></td><td>Yoruba</td></tr> 
<tr><td><img src="flags/south africa.gif" alt="South Africa Flag" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_Zulu.asp">Zulu</a></td><td><a href="samples/zulu-zu.docx">http://www.duxburysystems.com/samples/zulu-zu.docx</a></td><td>Zulu</td></tr> 
</table>

<p><hr /></p>
<img src="samples/rainban.gif" alt="ribbon graphic"><br />
<h1>Specialized English Demonstration Files</h1>
<table class=lead border="2" cellspacing="6">
<tr><th>Flags</th><th>Language</th><th>Sample File URL</th><th>DBT Template Name</th><th>Comment</th></tr>
<tr><td><img src="flags/united states.gif" alt="Flag of the United States" width="40" height="24" border="1"></td><td><a href="http://www.duxburysystems.com/lan_English_american_textbook.asp">English</a></td><td><a href="samples/styles.doc">http://www.duxburysystems.com/samples/styles.doc</a></td><td>English (American Textbook DE) - BANA</td><td>styles from Susan Christensen template<br />are properly imported into DBT</td></tr>

</table>

</body>
</html>

