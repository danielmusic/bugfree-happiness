
/*
 * Rascals Themes Effects Scripts
 *
 * Copyright (c) 2014
 * Rascals Themes 2014
 *
 */
 

/*
 * Thumb slider ver. 1.0.0
 * jQuery Thumb slider effect
 *
 * Copyright (c) 2014
 * Rascals Themes 2014
 *
 */
(function($){

	 $.fn.thumbSlider = function(options) {

		var 
			$this = $(this),
			mouseX = 0,
			mouseY = 0;
		

		// If "thumb-slide" has two images
		if ($('img', $this).length > 1) {

			// Check mouse position
			$('body').mousemove(function(e){
	   			mouseX = e.pageX // gives X position
	   			mouseY = e.pageY // gives Y position
			});

			$(window).resize(function() {
					$('.thumbs-wrap img:last-child').css({visibility : 'hidden'})
			});

			var 
				$this,
				$thumb,
				$hoverThumb,
				$height,
				$width,
				$wrap,
				$enterFrom,
				$leaveFrom;

			// Hover event
			$this.on('mouseenter', function(e) {

				$this       = $(this),
				$wrap 		= $('.thumbs-wrap', $this),
				$thumb      = $('img:first-child', $this),
				$hoverThumb = $('img:last-child', $this),
				$height 	= $thumb.height(),
				$width	 	= $thumb.width(),
				$enterFrom  = enterFrom($this);

				//console.log("enter from: " + enterFrom($this));

				// Add fixed width and height to the thumb image 
				$this.width($width);
				$this.height($height);
				$thumb.height($height);
				$thumb.width($width);
				$hoverThumb.height($height);
				$hoverThumb.width($width);
				$wrap.height($height*2);
				$wrap.width($width*2);

				// Add initial styles to thumb image and wrapper
				$thumb.css({
					position : 'absolute',
					top : 0,
					left : 0
				});
				$wrap.css({
					position : 'absolute',
					top: 0,
					left: 0
				});

				// Show direct animate

				// From top
				if ($enterFrom == 'top') {

					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : 0, left : 0 });
					$hoverThumb.css({ top : -$height + 'px', left : 0, visibility : 'visible'});

					// Animate
					$wrap.stop().animate({ top : $height + 'px' }, 400, 'easeOutQuart');
				}
				// From bottom
				else if ($enterFrom == 'bottom') {

					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top :  0, left : 0 });
					$hoverThumb.css({ top : $height + 'px', left : 0, visibility : 'visible'});

					// Animate
					$wrap.stop().animate({ top : -$height + 'px' }, 400, 'easeOutQuart');
				}
				// From left
				else if ($enterFrom == 'left') {

					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : 0, left : '0' });
					$hoverThumb.css({ top : 0, left : -$width + 'px', visibility : 'visible'});

					// Animate
					$wrap.stop().animate({ left : $width + 'px'}, 400, 'easeOutQuart');
				}
				// From right
				else if ($enterFrom == 'right') {

					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : 0, left : 0 });
					$hoverThumb.css({ top : 0, left : $width + 'px', visibility : 'visible'});

					// Animate
					$wrap.stop().animate({ left : -$width + 'px'}, 400, 'easeOutQuart');
				}
		
			
			}).on('mouseleave', function(e) {
				
				$leaveFrom  = leaveFrom($this, e);

				//console.log("leave from: " + leaveFrom($this, e));

				// From top
				if ($leaveFrom == 'top') {
					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : $height + 'px', left : 0 });
					$hoverThumb.css({ top : -$height + 'px', left : 0 });

					// Animate
					$wrap.stop().animate({ top : 0 }, 400, 'easeOutQuart');
					
				}
				// From bottom
				else if ($leaveFrom == 'bottom') {
					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : -$height + 'px', left : 0 });
					$hoverThumb.css({ top : $height + 'px', left : 0 });

					// Animate
					$wrap.stop().animate({ top : 0 }, 400, 'easeOutQuart');
					
				}
				// From left
				else if ($leaveFrom == 'left') {
					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : 0, left : $width + 'px' });
					$hoverThumb.css({ top : 0, left : -$width + 'px' });

					// Animate
					$wrap.stop().animate({ left : 0 }, 400, 'easeOutQuart');
					
				}
				// From right
				else if ($leaveFrom == 'right') {
					// Set Hover thumb position and thumbs wrap
					$wrap.css({ top : 0, left : -$width + 'px' });
					$hoverThumb.css({ top : 0, left : $width + 'px' });

					// Animate
					$wrap.stop().animate({ left : 0 }, 400, 'easeOutQuart');
					
				}
			
				// Reset styles
				$thumb.css({ 
					position : 'relative',
					height : 'auto',
					width : 'auto'

				});
				$wrap.css({ 
					position : 'relative',
					height : 'auto',
					width : 'auto'
				});
				$this.css({
					width : 'auto',
					height : 'auto'
				});
			
			});

		}

		// Detect mouse direction

		// Enter from
		function enterFrom(el) {
			var 
				$direction = 'top',
				$pos       = el.offset();

			// For IE browsers
			if (/msie [1-8]./.test(navigator.userAgent.toLowerCase())) {
				if ((mouseX-Math.round(el.width()/4)) <= $pos.left) return $direction = 'left';
				if ((mouseX+Math.round(el.width()/4)) >= ($pos.left + el.width())) return $direction = 'right';
				if (((mouseX+Math.round(el.width()/4)) >= $pos.left) && ((mouseY-Math.round(el.height()/4)) <= $pos.top)) return $direction = 'top';
				if (((mouseX-Math.round(el.width()/4)) >= $pos.left) && ((mouseY+Math.round(el.height()/4)) >= ($pos.top + el.height()))) return $direction = 'bottom';
			}
			
			if (mouseX <= $pos.left) return $direction = 'left';
			if (mouseX >= ($pos.left + el.width())) return $direction = 'right';
			if ((mouseX >= $pos.left) && (mouseY <= $pos.top)) return $direction = 'top';
			if ((mouseX >= $pos.left) && (mouseY >= ($pos.top + el.height()))) return $direction = 'bottom';

			return $direction;
		}

		// Leave from
		function leaveFrom(el, e) {
			var 
				$direction = 'top',
				$pos       = el.offset(),
				$mouseX    = e.pageX,
				$mouseY    = e.pageY;

			if ($mouseX <= $pos.left) return $direction = 'left';
			if ($mouseX >= ($pos.left + el.width())) return $direction = 'right';
			if (($mouseX >= $pos.left) && ($mouseY <= $pos.top)) return $direction = 'top';
			if (($mouseX >= $pos.left) && ($mouseY >= ($pos.top + el.height()))) return $direction = 'bottom';

			return $direction;
		}

	}

})(jQuery);


/*
 * Thumb Glitch ver. 1.0.0
 * jQuery glitch effect
 *
 * Copyright (c) 2014
 * Rascals Themes 2014
 *
 */

(function($){

	 $.fn.thumbGlitch = function(options) {

		$( this ).on( 'mouseenter', '.thumb-glitch > .hoverlayer', function(e){

			this.mouseout = false;
	        var el = this,
	        	icon_class = $( el ).parent().data( 'thumbicon' ),
	        	wrap = $( el ).parent().find( '.img' ),
	            img = $( 'img', wrap ).attr( 'style', '' ),
	            img_glitch_t = img.clone().addClass( 'img-glitch img-glitch-top' ).appendTo( wrap ),
	            img_glitch_b = img.clone().addClass( 'img-glitch img-glitch-bottom' ).appendTo( wrap ),
	            line_t = $( '<span>' ).addClass( 'line line-top' ).appendTo(wrap),
	            line_b = $( '<span>' ).addClass( 'line line-bottom' ).appendTo(wrap),
	            icon_l = $( '<span>' ).addClass( 'icon icon-left' ).addClass( icon_class ).appendTo( wrap ),
	            icon_r = $( '<span>' ).addClass( 'icon icon-right' ).addClass( icon_class ).appendTo( wrap );

	        // Animate effects
	        img_glitch_t.add( img_glitch_b ).animate({
	            top: 0,
	            opacity: 0.2
	        }, 300, 'easeOutElastic', function () {
	            $(this).remove()
	        });
	        line_t.animate({
	            width: '100%',
	            left: '0%'
	        }, 300, 'easeInExpo', function () {
	            $(this).remove()
	        });
	        line_b.animate({
	            width: '100%',
	            left: '0%'
	        }, 300, 'easeInExpo', function () {

	            $( this ).remove();
	            if ( ! el.mouseout ) {
	                img.stop().animate({
	                    opacity: 0.2
	                }, 200 );
	                icon_l.add( icon_r ).animate({
	                    opacity: 1,
	                    left: "50%"
	                }, 150, 'easeOutExpo')
	            }
	        });

		}).on( 'mouseleave', '.thumb-glitch > .hoverlayer', function(e){
			this.mouseout = true;
			$( this ).parent().find( '.img-glitch, .line, .icon' ).remove();
			$( this ).parent().find( 'img' ).animate({
				opacity: 1
			}, 200, function () {
				$( this ).attr('style', '')
			});

		});
	}


})(jQuery);


/*
 * TopTip ver. 1.1.0
 * jQuery Tooltip effect
 *
 * Copyright (c) 2014
 * Rascals Themes 2014
 *
 */
 

(function($){

   $.fn.topTip = function(options) {

	   	//Set the default values, use comma to separate the settings, example:
		var defaults = {
			syntax : ''
		}
			
		var options =  $.extend(defaults, options);

	    $(this).on('mouseenter', function(e) {
			// Add tip object
			var 
				tip = {},
				title = '',
				min_width = 200;
				mouse_move = false,
				tip = { 
				'desc' : $(this).data('tip-desc'),
				'top' : $(this).offset().top,
				'content' :  $(this).find('.tip-content').html()
			};

			// Check if title is exists
			if (tip.content == undefined) return;

			// Append datatip prior to closing body tag
			$('body').append('<div id="tip"><div class="tip-content"><div class="tip-inner">'+tip.content+'</div></div></div>');

			// Set max width
			if ($(this).outerWidth() > min_width) {
				$('#tip .tip-inner').width($(this).outerWidth()-40);
			}

			// Store datatip's height and width for later use
			tip['h'] = $('#tip div:first').outerHeight()+100;
			tip['w'] = $('#tip div:first').outerWidth();

			// Set datatip's mask properties - position, height, width etc  
			$('#tip').css({position:'absolute', overflow:'hidden', width:'100%', top:tip['top']-tip['h'], height:tip['h'], left:0 });

			// Mouse Move
			if (mouse_move) {
				// Set tip position
				$('#tip div').css({ left:e.pageX-(tip['w']/2), top:tip['h']+5 }).animate({ top:100 }, 500, 'easeOutBack');

				// Move datatip according to mouse position, whilst over instigator element
				$(this).mousemove(function(e){ $('#tip div').css({left: e.pageX-(tip['w']/2)}); }); 
			} else {
				// Set tip position
				var pos =  $(this).offset();
				$('#tip div').css({ left: pos.left+'px', top:tip['h']+5 }).animate({ top:100 }, 500, 'easeOutBack');
			}


	    }).on('mouseleave click', function(e) {

	      	// Remove datatip instances
	    	$('#tip').remove(); 

	    });
	
	}

})(jQuery);


/*
 * ResVid ver. 1.0.0
 * jQuery Responsive Video Plugin
 *
 * Copyright (c) 2014
 * Rascals Themes 2014
 *
 */

(function($){

 	$.fn.extend({ 
 		
		//pass the options variable to the function
 		ResVid: function(options) {


			//Set the default values, use comma to separate the settings, example:
			var defaults = {
				syntax : ''
			}
				
			var options =  $.extend(defaults, options);

    		return $('iframe', this).each(function(i) {
				var 
					$o = options,
					$iframe = $(this);
					$players = /www.youtube.com|player.vimeo.com/;
				
				if ($iframe.attr('src').search($players) > 0) {

					// Ratio
					var $ratio = ($iframe.height() / $iframe.width()) * 100;

					// Add some CSS to iframe
					$iframe.css({
						position : 'absolute',
						top : '0',
						left : '0',
						width : '100%',
						height : '100%'
					});

					// Add wrapper element
					$iframe.wrap('<div class="video-wrap" style="width:100%;position:relative;padding-top:'+$ratio+'%" />');
				}
				
				
			
    		});
    	}
	});
	
})(jQuery);