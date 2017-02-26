<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" <?php language_attributes(); ?>>
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="<?php bloginfo('html_type'); ?>; charset=<?php bloginfo('charset'); ?>" />
<title><?php elegant_titles(); ?></title>
<?php elegant_description(); ?>
<?php elegant_keywords(); ?>
<?php elegant_canonical(); ?>

<!--<link href='http://fonts.googleapis.com/css?family=Droid+Sans:regular,bold' rel='stylesheet' type='text/css' />-->
<link href='http://fonts.googleapis.com/css?family=Raleway:400,700,900' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="<?php bloginfo('stylesheet_url'); ?>" type="text/css" media="screen" />
<link rel="alternate" type="application/rss+xml" title="<?php bloginfo('name'); ?> RSS Feed" href="<?php bloginfo('rss2_url'); ?>" />
<link rel="alternate" type="application/atom+xml" title="<?php bloginfo('name'); ?> Atom Feed" href="<?php bloginfo('atom_url'); ?>" />
<link rel="pingback" href="<?php bloginfo('pingback_url'); ?>" />

<!--[if lt IE 7]>
	<link rel="stylesheet" type="text/css" href="<?php bloginfo('template_directory'); ?>/css/ie6style.css" />
	<script type="text/javascript" src="<?php bloginfo('template_directory'); ?>/js/DD_belatedPNG_0.0.8a-min.js"></script>
	<script type="text/javascript">DD_belatedPNG.fix('img#logo, ul.nav li.backLava, ul.nav li.backLava div.leftLava, #menu, #search-form, #featured, div.slide div.overlay, a#left-arrow, a#right-arrow, div.description, a.readmore, a.readmore span, ul.nav ul li a, #content-bottom, div.service img.service-icon, #controllers, #controllers-top, #controllers-main, #controllers a span.tooltip span.left-arrow, span.overlay, div.hr, #content-top, div.top-alt, div.bottom-alt, #content-bottom, #breadcrumbs span.sep');</script>
<![endif]-->
<!--[if IE 7]>
	<link rel="stylesheet" type="text/css" href="<?php bloginfo('template_directory'); ?>/css/ie7style.css" />
<![endif]-->
<!--[if IE 8]>
	<link rel="stylesheet" type="text/css" href="<?php bloginfo('template_directory'); ?>/css/ie8style.css" />
<![endif]-->

<script type="text/javascript">
	document.documentElement.className = 'js';
</script>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
<!-- fonction swap hover on off 2 images -->
<script type="text/javascript">
    $(document).ready(function() {

         // pour refuser les lettres sur les champs input tél et CP
		 $('input[name="your-cp"],input[name="your-phone"],#contact-cp,#contact-telephone').keypress(function(e) { /* pour les champs qui ne prennent que du numeric en entrée */
             var key = e.charCode || e.keyCode || 0;
             var keychar = String.fromCharCode(key);
             /*alert(« keychar: »+keychar +  » \n charCode: » + e.charCode +  » \n key: » +key);*/
             if (  ((key == 8 || key == 9 || key == 46 || key == 35 || key == 36 || (key >= 37 && key <= 40)) && e.charCode==0) /* backspace, end, begin, top, bottom, right, left, del, tab */
                     || (key >= 48 && key <= 57) ) { /* 0-9 */
                 return;
             } else {
                 e.preventDefault();
             }
         });



        $("img", this).hover(swapImageIn, swapImageOut);

        function swapImageIn(e) {
            this.src = this.src.replace("-off", "-on");
        }
        function swapImageOut (e) {
            this.src = this.src.replace("-on", "-off");
        }

        $("input",this).click(function(){
        				$(this).val("");
        });

        //récupération ancre pour illuminer le pack
	var url = document.location.toString();
	if (url.match('#')) {
	    var anchor =url.split('#')[1];
	    switch(anchor){
	    	case 'essentiel': $(".wp-image-98").css("border","1px #ff0000 solid");
	    	$(".wp-image-97").css({ opacity: 0.5 });
	    	$(".wp-image-99").css({ opacity: 0.5 });
	    	break;
	    	case 'confort': $(".wp-image-97").css("border","1px #ff0000 solid");
	    	$(".wp-image-98").css({ opacity: 0.5 });
	    	$(".wp-image-99").css({ opacity: 0.5 });
	    	break;
	    	case 'premium': $(".wp-image-99").css("border","1px #ff0000 solid");
	    	$(".wp-image-97").css({ opacity: 0.5 });
	    	$(".wp-image-98").css({ opacity: 0.5 });
	    	break;
	     }
	}

	});

	function verif_nombre(champ)
  {
	var chiffres = new RegExp("[0-9]");
	var verif;
	var points = 0;

	for(x = 0; x < champ.value.length; x++)
	{
            verif = chiffres.test(champ.value.charAt(x));
	    if(champ.value.charAt(x) == "."){points++;}
            if(points > 1){verif = false; points = 1;}
  	    if(verif == false){
  	    champ.value = champ.value.substr(0,x) + champ.value.substr(x+1,champ.value.length-x+1);
  	    x--;
  	    }
	}
  }

</script>
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-36504879-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>



<?php if ( is_singular() ) wp_enqueue_script( 'comment-reply' ); ?>
<?php wp_head(); ?>

</head>
<body<?php if (is_home()) echo(' id="home"'); ?> <?php body_class(); ?>>
	<div id="container">
		<div id="header">
            <div id="login">
			<form id="loginform" method="post" action="http://extranet.ics.fr/login_externe.php" name="loginform" target="_blank">
				<input id="groupe" value="syndiceo" type="hidden" name="groupe">
				<input id="user_login" class="input" type="text" size="20" value="Identifiant" name="login">
				<input id="user_pass" class="input" type="password" size="20" value="Mot de passe" name="mdp">
				<input id="wp-submit" class="wpcf7-submit" type="submit" value="Se connecter" name="wp-submit">
				</form>
			</div>
			<span class="logolink">
                <a href="<?php bloginfo('url'); ?>"><?php $logo = (get_option('professional_logo') <> '') ? get_option('professional_logo') : get_bloginfo('template_directory').'/images/logo.png'; ?>
					<img src="<?php echo esc_url($logo); ?>" alt="<?php echo esc_attr(get_bloginfo('name')); ?>" id="logo"/></a>
             </span>

			<div id="menu">
				<?php $menuClass = 'nav';
				$primaryNav = '';
				if (function_exists('wp_nav_menu')) {
					$primaryNav = wp_nav_menu( array( 'theme_location' => 'primary-menu', 'container' => '', 'fallback_cb' => '', 'menu_class' => $menuClass, 'echo' => false ) );
				};
				if ($primaryNav == '') { ?>
					<ul class="<?php echo $menuClass; ?>">
						<?php if (get_option('professional_home_link') == 'on') { ?>
							<li <?php if (is_home()) echo('class="current_page_item"') ?>><a href="<?php bloginfo('url'); ?>"><?php esc_html_e('Home','Professional') ?></a></li>
						<?php }; ?>

						<?php show_page_menu($menuClass,false,false); ?>
						<?php show_categories_menu($menuClass,false); ?>
					</ul> <!-- end ul.nav -->
				<?php }
				else echo($primaryNav); ?>

				<?php global $default_colorscheme, $shortname; $colorSchemePath = '';
				$colorScheme = get_option($shortname . '_color_scheme');
				if ($colorScheme <> $default_colorscheme) $colorSchemePath = strtolower($colorScheme) . '/'; ?>

				<div id="search-form">
					<form method="get" id="searchform" action="<?php echo home_url(); ?>">
						<input type="text" value="<?php esc_attr_e('search this site...','Professional'); ?>" name="s" id="searchinput" />

						<input type="image" src="<?php bloginfo('template_directory'); ?>/images/<?php echo esc_attr($colorSchemePath); ?>search_btn.png" id="searchsubmit" />
					</form>
				</div> <!-- end #search-form -->
			</div> <!-- end #menu -->
			<div id="logo-home"><a href="http://www.syndiceo.com"><img src="<?php bloginfo('template_directory'); ?>/images/logo-small.gif"></a></div>
		</div> <!-- end #header -->

		<?php if (get_option('professional_featured') == 'on' && (is_home() || is_front_page())) get_template_part('includes/featured'); ?>
