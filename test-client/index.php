<?php
	
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	
	function msg($msg) {
		echo "<h4>$msg</h4>";
	}

	define('CONVERT', 'Convert to Xliff');
	define('ORIGINAL', 'Extract the original file');
	define('DERIVED', 'Generate the derive file');
	define('OUTDIR', 'out');

	if (isset($_POST['action'])) {

		msg('Form sent, trying to process the file...');
		

		$ip = $_POST['ip'];
		$port = $_POST['port'];
		$srcLan = isset($_POST['srcLan'])? $_POST['srcLan'] : '';
		$trgLan = isset($_POST['trgLan'])? $_POST['trgLan'] : '';
		$file = $_FILES['file'];
		$filename = $file['name'];
		$tmpFile = $file['tmp_name'];

		msg("File $filename received, temporally stored at $tmpFile");

		$url = "";
		switch ($_POST['action']) {
			case CONVERT: $url = "http://$ip:$port/convert/$srcLan/$trgLan"; break;
			case ORIGINAL: $url = "http://$ip:$port/original"; break;
			case DERIVED: $url = "http://$ip:$port/derived"; break;
		}

		msg("CURLing $url");
	
		$ch = curl_init();
	    $cfile = curl_file_create($tmpFile,$file['type'],$filename);

		// Assign POST data
		$data = array('file' => $cfile);
		curl_setopt($ch, CURLOPT_POST,1);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
        curl_setopt( $ch, CURLOPT_URL, $url );
        curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
        $output     = curl_exec( $ch );
        $curl_errno = curl_errno( $ch );
        $curl_error = curl_error( $ch );
        $info       = curl_getinfo( $ch );
        curl_close( $ch );

	    if ( $curl_errno > 0 ) {
	        msg("<span style='color: red;'>CURL error ($curl_errno): $curl_error</span>");
	    }
	    else {
	    	msg("<p>CURL SUCCESS!</p>");
	    	$result = json_decode( $output, true );
		    if ($result['isSuccess']) {
		    	$outputDir = getcwd().'/'.OUTDIR;
		    	if (!file_exists($outputDir))
		    		mkdir($outputDir);
		    	$outputPath = $outputDir.'/'.$result['filename'];
		    	$content = base64_decode($result['documentContent']);      
		        file_put_contents($outputPath, $content);
		        msg("Done! Saved in: $outputDir");
		    } 
		    else {
		        msg("<span style='color: red;'>Conversion error: {$result['errorMessage']}</span>");
		    }
	    }

	    msg("<hr/>");
    }
	
?>

<form method="post" enctype="multipart/form-data">
	IP: <input type="text" name="ip" value="localhost"/><br/>
	PORT: <input type="text" name="port" value="8082"/><br/>
	<br/>
	SOURCE LANGUAGE: <input type="text" name="srcLan" value="en-US"/><br/>
	TARGET LANGUAGE: <input type="text" name="trgLan" value="it-IT"/><br/>
	<br/>
	FILE: <input type="file" name="file"><br/>
	<br/>
	<input type="submit" name="action" value="<?=CONVERT?>"/>
	<input type="submit" name="action" value="<?=ORIGINAL?>"/>
	<input type="submit" name="action" value="<?=DERIVED?>"/>
</form>