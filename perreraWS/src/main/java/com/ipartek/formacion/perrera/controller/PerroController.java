package com.ipartek.formacion.perrera.controller;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ipartek.formacion.perrera.pojo.Perro;
import com.ipartek.formacion.perrera.service.PerroServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * El poryecto hace refencia al proyecto skalada
 *
 * @author Curso
 *
 */
@Path("/perro")
@Api(value = "/perro")
public class PerroController {
	private final Logger logger = LoggerFactory.getLogger(PerroController.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Listado de Perros", notes = "Listado de perros existentes en la perrera, limitado a 1.000", response = Perro.class, responseContainer = "List")

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getAll(
			@ApiParam(name = "orderBy", required = false, value = "Filtro para ordenar los perros de forma ascendente o descendente, posibles valores [asc|desc]") @DefaultValue("asc") @QueryParam("orderBy") String orderBy,
			@ApiParam(name = "campo", required = false, value = "Filtro para ordenar por 'campo' los perros, posibles valores [id|nombre|raza]") @DefaultValue("id") @QueryParam("campo") String campo) {
		try {
			this.logger.info("preparando la instancia...");
			PerroServiceImpl dao = PerroServiceImpl.getInstance();
			this.logger.info("procesando peticion para listar perros");
			ArrayList<Perro> perros = (ArrayList<Perro>) dao.getAll(orderBy, campo);
			this.logger.info("perros listados correctamente");
			return Response.ok().entity(perros).build();

		} catch (Exception e) {
			this.logger.error("Error al listar los perros");
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca un perro por su ID", notes = "devuelve un perro mediante el paso de su ID", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe perro con esa ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getById(@PathParam("id") int idPerro) {

		try {
			this.logger.info("preparando la instancia...");
			PerroServiceImpl dao = PerroServiceImpl.getInstance();
			Perro perro = null;
			this.logger.info("procesando peticion para encontrar al perro con id:" + idPerro);
			perro = dao.getById(idPerro);
			if (perro == null) {
				this.logger.info("El perro con id:" + idPerro + " no existe");
				return Response.noContent().build();
			}
			this.logger.info("El perro con id:" + idPerro + " existe y esta siendo mostrado");
			return Response.ok().entity(perro).build();
		} catch (Exception e) {
			this.logger.error("Error en la busqueda del perro con id:" + idPerro);
			return Response.serverError().build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Elimina un perro", notes = "Elimina un perro mediante el paso de su ID", response = Perro.class, responseContainer = "FechaHora")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Perro eliminado"),
			@ApiResponse(code = 204, message = "No existe Perro con ese ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response delete(@PathParam("id") long idPerro) {

		try {
			this.logger.info("preparando la instancia...");
			PerroServiceImpl dao = PerroServiceImpl.getInstance();
			boolean pElimnar = false;
			this.logger.info("procesando peticion para eliminar al perro con id:" + idPerro);
			pElimnar = dao.delete(idPerro);
			if (pElimnar != true) {
				this.logger.info("No hay ningun perro con id:" + idPerro + ", por lo tanto no se ha eliminado nada");
				return Response.notModified().build();
			} else {
				this.logger.info("El perro con id:" + idPerro + " ha sido correctamente eliminado");
				return Response.ok().entity(pElimnar).build();
			}
		} catch (Exception e) {
			this.logger.error("Error en la eliminacion del perro con id:" + idPerro);
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/{nombre}/{raza}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Añade un perro", notes = "Crea y persiste un nuevo perro", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Perro Creado con exito"),
			@ApiResponse(code = 304, message = "No se ha podido modificar al perro"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response post(@PathParam("nombre") String nombrePerro, @PathParam("raza") String razaPerro) {
		try {
			this.logger.info("preparando la instancia...");
			PerroServiceImpl dao = PerroServiceImpl.getInstance();
			Perro pCreado = new Perro(nombrePerro, razaPerro);
			boolean idpCreado = false;
			this.logger.info("procesando peticion para crear un nuevo perro");
			idpCreado = dao.insert(pCreado);
			if (idpCreado == true) {
				this.logger.info("El perro ha sido creado correctamente");
				return Response.status(201).entity(pCreado).build();
			} else {
				this.logger.info("El perro no ha podido ser dado de alta porque ya existe un perro con esos datos");
				return Response.status(304).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("Error en el alta de perro");
			return Response.serverError().build();
		}
	}

	@PUT
	@Path("/{id}/{nombre}/{raza}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifica un perro", notes = "Modifica un perro ya existente mediante su identificador", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe perro con ese ID"),
			@ApiResponse(code = 304, message = "No se ha podido modificar al perro"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response put(@PathParam("id") long idPerro, @PathParam("nombre") String nombrePerro,
			@PathParam("raza") String razaPerro) {
		try {
			this.logger.info("preparando la instancia...");
			PerroServiceImpl dao = PerroServiceImpl.getInstance();
			Perro pModificar = new Perro(idPerro, nombrePerro, razaPerro);

			boolean rModificar = false;
			this.logger.info("procesando peticion para modificar al perro con id" + idPerro);
			rModificar = dao.update(pModificar);
			if (rModificar != true) {
				this.logger.info("EL perro con id" + idPerro + " no ha podido ser modificado");
				return Response.notModified().build();
			} else {
				this.logger.info("El perro con id" + idPerro + " ha sido modificado correctamente");
				return Response.ok().entity(pModificar).build();
			}
		} catch (Exception e) {
			this.logger.error("Error al modificar al perro con id" + idPerro);
			return Response.status(500).build();

		}
	}
}