

package eBIS.AppConfig;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SuppressWarnings({ "unchecked" })
public class SecondaryDaoHelper {

	@Autowired
	@PersistenceContext(unitName="unitreadonly")
	@Qualifier(value="myEmfread")
	private EntityManager entityManager;

	public SecondaryDaoHelper() {
	}

	public SecondaryDaoHelper(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public synchronized SessionFactory transactionsession() {
		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		SessionFactory sf = entityManager.unwrap(Session.class).getSessionFactory();
		return sf;
	}

	public <T> List<T> findByQuery(final String hql) {
		Query query=null;
		try {
			query = entityManager.createQuery(hql);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			entityManager.clear();	
		}
		return castResultList(query.getResultList());
	}
	public Query hqlQuery(final String hql) {
		Query query=null;
		try {
			query = entityManager.createQuery(hql);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			entityManager.clear();	
		}
		return query;
	}

	private synchronized <T> List<T> castResultList(List<?> results) {
		return (List<T>) results;
	}

}
