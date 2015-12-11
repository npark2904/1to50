<?php
$Version = 1.11;

$config['db_host'] = "localhost";
$config['db_user'] = "iamgc";
$config['db_pass'] = "tlssua1";
$config['db_name'] = "iamgc";

$page = $_POST['PAGE'] * 10;
$db_table = "game_1to50db";
$command = $_POST['Command'];
$filename = "pgc_test";

if(strcmp($command,"LOAD")==0){
	$conn = mysql_connect($config['db_host'], $config['db_user'], $config['db_pass']) or die("<!-- connect_error() : " . mysql_error() . " -->");
	mysql_select_db($config['db_name'], $conn) or die ("<!-- db_select_error() : " . mysql_error() . " -->");

	header('Content-type: text/xml; charset=utf-8');
	$xmlString .= "<?xml version=\"1.0\" encoding =\"utf-8\"?>\n";
	$xmlString .= "<ScoreData>\n";
	
	$sql = "SELECT count(*) from " . $db_table;
	$result = mysql_query($sql, $conn) or die ("<!-- sql_error() : " . mysql_error() . " -->");
	$row = mysql_fetch_array($result);
	$xmlString .= "\t<TotalDataCount>$row[0]</TotalDataCount>\n";

	$sql = "SELECT * from " . $db_table . " ORDER BY " . $db_table .".score ASC LIMIT $page, 10";
	$result = mysql_query($sql, $conn) or die ("<!-- sql_error() : " . mysql_error() . " -->");
	$max_count = mysql_num_rows($result);
	
	while($row = mysql_fetch_array($result)){
		$xmlString .= "\t<Person ";
		$xmlString .= "name=\"$row[name]\" ";
		$xmlString .= "score=\"$row[score]\" ";
		$xmlString .= "comment=\"$row[comment]\" ";
		$xmlString .= "date=\"$row[date]\">";
		$xmlString .= "</Person>\n";
    	}
	$xmlString .= "</ScoreData>\n";
	
	echo $xmlString;
	
	mysql_close($conn);
	
} else if(strcmp($command,"SAVE")==0){
	$conn = mysql_connect($config['db_host'], $config['db_user'], $config['db_pass']) or die("<!-- connect_error() : " . mysql_error() . " -->");
	mysql_select_db($config['db_name'], $conn) or die ("<!-- db_select_error() : " . mysql_error() . " -->");
	
	$sql = "Insert game_1to50db set name='$_POST[NAME]', score='$_POST[SCORE]', comment='$_POST[COMMENT]', date=sysdate()";
	$result = mysql_query($sql, $conn) or die ("<!-- sql_error() : " . mysql_error() . " -->");
	mysql_close($conn);
	echo "#complete";
} else if(strcmp($command,"NOTICE")==0){
	header('Content-type: text/xml; charset=utf-8');
	$xmlString .= "<?xml version=\"1.0\" encoding =\"utf-8\"?>\n";
	$xmlString .= "<ScoreData>\n";
	$xmlString .= "\t<Version>$Version</Version>\n";
	$xmlString .= "\t<Date>2013/11/15</Date>\n";
	$xmlString .= "\t<Issue>점점 판이 커지고 있다.........ㅋㅋ</Issue>\n";
	$xmlString .= "</ScoreData>\n";
	echo $xmlString;
}
?>