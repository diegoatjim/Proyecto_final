<?php
include "config.php";
include "utils.php";

$dbConn =  connect($db);

if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['codigo']))
    {
      //Mostrar un post
      $sQuery = "SELECT * FROM ticket where tic_id =:codigo";
      $sql = $dbConn->prepare($sQuery);
      $sql->bindValue(':codigo', $_GET['codigo']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll());
      exit();
    }

    else {
      //Mostrar lista de post
      $sql = $dbConn->prepare("SELECT * FROM ticket");
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
    $sql = "INSERT INTO ticket(usu_id, tic_placa, tic_fecha_ingreso, tic_foto, tic_fecha_salida, tic_tiempo, tic_valor, tic_total_pago, tic_estado)
          VALUES (:usuario, :placa,:ingreso, :foto, :salida, :tiempo, :valor , :pago, :estado)";
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
      echo json_encode($input);
      exit();
   }
}
//delete
if ($_SERVER['REQUEST_METHOD'] == 'DELETE')
{
  $codigo = $_GET['codigo'];
  $statement = $dbConn->prepare("DELETE FROM  ticket where tic_id=:codigo");
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
          UPDATE ticket
          SET $fields
          WHERE tic_id='$postCodigo'
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
