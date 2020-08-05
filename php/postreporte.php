<?php
include "config.php";
include "utils.php";

$dbConn =  connect($db);

if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['codigo']))
    {
      //Mostrar un post
      $sQuery = "select fac_numero, cli_nombre, tic_placa, tic_fecha_ingreso, tic_fecha_salida, tic_tiempo, fac_valor, 
	  tic_valor, fac_iva, fac_total from factura, cliente, ticket where factura.cli_id = cliente.cli_id 
	  and factura.tic_id = ticket.tic_id and fac_id =:codigo";
      $sql = $dbConn->prepare($sQuery);
      $sql->bindValue(':codigo', $_GET['codigo']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");

      //echo json_encode($sql->fetch(PDO::FETCH_ASSOC)  );
      echo json_encode( $sql->fetchAll());
      exit();
    }

    else {
      //Mostrar lista de post
      $sql = $dbConn->prepare("select fac_numero, cli_nombre, tic_placa, tic_fecha_ingreso, tic_fecha_salida, tic_tiempo, 
	  tic_valor, fac_valor, fac_iva, fac_total from factura, cliente, ticket where factura.cli_id = cliente.cli_id 
	  and factura.tic_id = ticket.tic_id");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode( $sql->fetchAll()  );
      exit();
  }
}
//En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");

?>
