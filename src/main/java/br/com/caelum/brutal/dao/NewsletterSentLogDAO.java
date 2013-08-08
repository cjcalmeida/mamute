package br.com.caelum.brutal.dao;

import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.NewsletterSentLog;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NewsletterSentLogDAO {
	private final Session session;

	public NewsletterSentLogDAO(Session session) {
		this.session = session;
	}
	
	public boolean wasSentThisWeek(){
		return session.createQuery("from NewsletterSentLog n where createdAt between :firstDay and :lastDay")
			   .setParameter("firstDay", new DateTime().withDayOfWeek(1))
			   .setParameter("lastDay", new DateTime().withDayOfWeek(7))
			   .uniqueResult() != null;
	}

	public void saveLog() {
		session.save(new NewsletterSentLog());
	}
}