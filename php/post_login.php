<?php
include "config.php";
include "utils.php";

$dbConn =  connect($db);

if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['nombre']) && isset($_GET['clave']))
    {
    /*  //Mostrar un post
      $sql = $dbConn->prepare("SELECT * from usuario  where usu_estado =1 and usu_nombre=:nombre and usu_clave=:clave");
      $sql->bindValue(':nombre', $_GET['nombre']);
	  $sql->bindValue(':clave', $_GET['clave']);
      $sql->execute();
      header("HTTP/1.1 200 OK");
      echo json_encode(  $sql->fetch(PDO::FETCH_ASSOC)  );
      exit();
	  */
	  //Mostrar un post
      $sQuery = "SELECT * from usuario  where usu_estado =1 and usu_nombre=:nombre and usu_clave=:clave";
      $sql = $dbConn->prepare($sQuery);
      $sql->bindValue(':nombre', $_GET['nombre']);
	  $sql->bindValue(':clave', $_GET['clave']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");

      //echo json_encode($sql->fetch(PDO::FETCH_ASSOC)  );
      echo json_encode( $sql->fetchAll());
      exit();
	 }
}

//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>
