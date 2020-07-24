<?php
include "config.php";
include "utils.php";

$dbConn =  connect($db);

if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['codigo']))
    {
      //Mostrar un post
      $sQuery = "SELECT * from cliente  where cli_id=:codigo";
      $sql = $dbConn->prepare($sQuery);
      $sql->bindValue(':codigo', $_GET['codigo']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");

      //echo json_encode($sql->fetch(PDO::FETCH_ASSOC)  );
      echo json_encode( $sql->fetchAll());
      exit();
    }
    elseif (isset($_GET['identificacion'])) {
       //Mostrar lista de post
      $sQuery = "SELECT * FROM cliente where cli_identificacion=:identificacion";
      $sql = $dbConn->prepare($sQuery);
      $sql->bindValue(':identificacion', $_GET['identificacion']);
     // echo $sQuery;
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll());
      exit();
    }
    // elseif (isset($_GET['nombre'])) {
    //    //Mostrar lista de post
    //   $sql = $dbConn->prepare("SELECT * FROM cliente where cli_identificacion = :nombre");
    //   $sql->execute();
    //   $sql->setFetchMode(PDO::FETCH_ASSOC);
    //   header("HTTP/1.1 200 OK");
    //   echo json_encode( $sql->fetchAll()  );
    //   exit();
    // }
    else {
      //Mostrar lista de post
      $sql = $dbConn->prepare("SELECT * FROM cliente ");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll()  );
      exit();
  }
}
//insert

if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $input = $_POST;
    $sql = "INSERT INTO cliente(cli_nombre, cli_tipo_id, cli_identificacion, cli_email, cli_telefono, cli_direccion)
          VALUES (:nombre, :tipo,:identificacion, :email, :telefono, :direccion)";
    $statement = $dbConn->prepare($sql);
    bindAllValues($statement, $input);
    $statement->execute();
     // $idInsert =  mysql_insert_id($dbConn);
      //echo $idInsert;

    $postCodigo = $dbConn->lastInsertId();
    //echo $postCodigo;
    if($postCodigo)
    {
      $input['codigo'] = $postCodigo;
      header("HTTP/1.1 200 OK");
      echo json_encode($postCodigo);
      exit();
   }
}
//delete
if ($_SERVER['REQUEST_METHOD'] == 'DELETE')
{
  $codigo = $_GET['codigo'];
  $statement = $dbConn->prepare("DELETE FROM  cliente where cli_id=:codigo");
  $statement->bindValue(':codigo', $codigo);
  $statement->execute();
  header("HTTP/1.1 200 OK");
  exit();
}
//update
if ($_SERVER['REQUEST_METHOD'] == 'PUT')
{
    $input = $_GET;
    $postCodigo = $input['cli_id'];
    $fields = getParams($input);

    $sql = "
          UPDATE cliente
          SET $fields
          WHERE cli_id='$postCodigo'
           ";

    $statement = $dbConn->prepare($sql);
    bindAllValues($statement, $input);

    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}
//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>
