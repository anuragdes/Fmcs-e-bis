package eBIS.AppConfig;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SuppressWarnings({ "unchecked" })
public class PrimaryDaoHelper {

	@Autowired
	@PersistenceContext(unitName="unitmain")
	@Qualifier(value="myEmf")
	private EntityManager entityManager;

	public PrimaryDaoHelper() {
	}

	public PrimaryDaoHelper(final EntityManager entityManager) {
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
	public <T1,T2> void  maintainRecordInLog(T1 t1, T2 t2)
	{
		
	}
	@SuppressWarnings("deprecation")
	public synchronized Connection getconnectName() {
		SessionFactory sf = transactionsession();
		SessionFactoryImplementor sessionFactoryImplementation = (SessionFactoryImplementor) sf;
		ConnectionProvider connectionProvider = sessionFactoryImplementation.getConnectionProvider();
		 Connection connection =null;
		try {
		    connection = connectionProvider.getConnection();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return connection;
	}
	
	public synchronized <T> int persist( T entity) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;
		int flag=-1;
		try {
			tx = opensession.beginTransaction();
			opensession.persist(entity);
			tx.commit();
			flag=1;
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			flag=0;
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
			
		}
		return flag;
	}
	public synchronized <T> T merge( T entity) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = opensession.beginTransaction();
			entity = (T) opensession.merge(entity);
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
			
		}
		return entity;
	}
	public synchronized <T> int persistList(List<T> entity) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;

		int flag=0;
		try {
			tx = opensession.beginTransaction();
			for(int i=0;i<entity.size();i++) {
				opensession.persist(entity.get(i));
			}
			tx.commit();
			flag=1;
		} catch (Exception ex) {
			tx.rollback();
			flag=0;
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
		}
		return flag;
	}

	public synchronized <T> int jdbcExecuteQueryChecker(String query) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;

		int flag=0;
		try {
			tx = opensession.beginTransaction();
			SQLQuery f = opensession.createSQLQuery(query);
			flag=f.executeUpdate();
		} catch (Exception ex) {
			tx.rollback();
			flag=0;
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();

		}
		return flag;
	}
	public synchronized <T> int jdbcExecuteQuery(String query) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;

		int flag=0;
		try {
			tx = opensession.beginTransaction();
			SQLQuery f = opensession.createSQLQuery(query);
			flag=f.executeUpdate();
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			flag=0;
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
		}
		return flag;
	}
	public synchronized <T> int updateList(List<T> entity) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;

		int flag=0;
		try {
			tx = opensession.beginTransaction();
			for(int i=0;i<entity.size();i++) {
				opensession.merge(entity.get(i));
				System.out.println("After Merge["+i+"]:"+entity.get(i));

			}
			tx.commit();
			flag=1;
		} catch (Exception ex) {
			tx.rollback();
			flag=0;
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
		}
		return flag;
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
	public synchronized <T> int updateAndDeleteList(List<T> entity,List<T> deleteEntity) {
		SessionFactory sessionFactory = transactionsession();
		Session opensession = sessionFactory.openSession();
		Transaction tx = null;

		int flag=0;
		try {
			tx = opensession.beginTransaction();
			if(entity!=null) {
			for(int i=0;i<entity.size();i++) {
				opensession.merge(entity.get(i));
				System.out.println("After Merge["+i+"]:"+entity.get(i));

			}
			}
			if(deleteEntity!=null) {
			for(int j=0;j<deleteEntity.size();j++) {
				opensession.delete(opensession.merge(deleteEntity.get(j)));
				System.out.println("After Delete["+j+"]:"+deleteEntity.get(j));
			}
			}
			tx.commit();
			flag=1;
		} catch (Exception ex) {
			tx.rollback();
			flag=0;
			ex.printStackTrace();
		} finally {
			opensession.clear();
			opensession.flush();
			opensession.close();
		}
		return flag;
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

	public int deleteByQuery(String hql) {
		int flag=0;
		try {
			flag=entityManager.createQuery(hql).executeUpdate();	
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			entityManager.clear();	
		}
		return flag;
	}
	
	public String getSQL(final String query) {
		Query query1 = entityManager.createQuery(query);
		String queryString = query1.unwrap(org.hibernate.Query.class).getQueryString();
		ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
		SessionImplementor hibernateSession = entityManager.unwrap(SessionImplementor.class);
		QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("", queryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory());
		queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
		String sqlQueryString = queryTranslator.getSQLString() ;
		return sqlQueryString;
	}
}
