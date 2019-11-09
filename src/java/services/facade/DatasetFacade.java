package services.facade;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Dataset;


@Stateless
public class DatasetFacade extends AbstractFacade<Dataset> implements Serializable {

	public DatasetFacade() {
		super(Dataset.class);
	}

	@PersistenceContext(unitName = "prjIntegrator")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
