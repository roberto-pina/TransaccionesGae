package com.jaime.movimientodebanco;

import com.jaime.movimientodebanco.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "transaccionendpoint", namespace = @ApiNamespace(ownerDomain = "jaime.com", ownerName = "jaime.com", packagePath = "movimientodebanco"))
public class TransaccionEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listTransaccion")
	public CollectionResponse<Transaccion> listTransaccion(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Transaccion> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Transaccion.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Transaccion>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Transaccion obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Transaccion> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getTransaccion")
	public Transaccion getTransaccion(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Transaccion transaccion = null;
		try {
			transaccion = mgr.getObjectById(Transaccion.class, id);
		} finally {
			mgr.close();
		}
		return transaccion;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param transaccion the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertTransaccion")
	public Transaccion insertTransaccion(Transaccion transaccion) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (transaccion.getId() != null) {
				if (containsTransaccion(transaccion)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			mgr.makePersistent(transaccion);
		} finally {
			mgr.close();
		}
		return transaccion;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param transaccion the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateTransaccion")
	public Transaccion updateTransaccion(Transaccion transaccion) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsTransaccion(transaccion)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(transaccion);
		} finally {
			mgr.close();
		}
		return transaccion;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeTransaccion")
	public void removeTransaccion(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Transaccion transaccion = mgr.getObjectById(Transaccion.class, id);
			mgr.deletePersistent(transaccion);
		} finally {
			mgr.close();
		}
	}

	private boolean containsTransaccion(Transaccion transaccion) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Transaccion.class, transaccion.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
