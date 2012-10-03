

<!doctype html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>blinklicht</title>
        <link rel="stylesheet" href="themes/css/jqtouch.css" title="jQTouch">

        <script src="src/jqtouch.min.js" type="text/javascript" charset="utf-8"></script>  
        <script src="src/lib/jquery-1.7.min.js" type="application/x-javascript" charset="utf-8"></script> 
        <script src="src/jqtouch-jquery.js" type="application/x-javascript" charset="utf-8"></script> 
        <script type="text/javascript" charset="utf-8">

            var jQT = new $.jQTouch({
                icon: 'jqtouch.png',
                icon4: 'jqtouch4.png',
                addGlossToIcon: false,
                startupScreen: 'jqt_startup.png',
                statusBar: 'black-translucent',
                themeSelectionSelector: '#jqt #themes ul',
                preloadImages: []
            });

            $(function(){				
				$('.execute_event').click(function(){
					$id = $(this).attr('id');
					$.get("blink/connection.php", { e: $id }, function(data){console.log(data);});				
				});
				$('.led_device').change(function(evt, data) {
					$id = $(this).attr('id');
					if($(this).children('input').attr('checked') == 'checked'){
					 $on = 1;
					}
                    else{
                     $on = 0;   
                    }	
                    $.get("blink/connection.php", { p: $id , b: $on}, function(data){console.log(data);});									
                });              
            });
        </script>
        <style type="text/css" media="screen">
            #jqt.fullscreen #home .info {
                display: none;
            }
            div#jqt #about {
                padding: 100px 10px 40px;
                text-shadow: rgba(0, 0, 0, 0.3) 0px -1px 0;
                color: #999;
                font-size: 13px;
                text-align: center;
                background: #161618;
            }
            div#jqt #about p {
                margin-bottom: 8px;
            }
            div#jqt #about a {
                color: #fff;
                font-weight: bold;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
            <div id="home" class="current">
                <div class="toolbar">
                    <h1>blinklicht</h1>
                </div><br>
				<div class="toolbar">
					<h1>Mode</h1>
                </div>
				
                <form class="scroll">
                    <ul class="edit rounded">
   
                        <li>Fade<span class="led_device toggle" id="fade"><input type="checkbox"/></span></li>  
                        <li>Random<span class="led_device toggle"  id="random"><input type="checkbox" /></span></li> 
                        <li>Constant<span class="led_device toggle"  id="constant"><input type="checkbox" /></span></li> 
                    </ul>
                </form>
				

			</div>
		</div>
            </div>

            
    </body>
</html>
