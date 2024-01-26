package vttp2023.batch4.paf.assessment.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	private final String LISTINGS_COLLECTION = "listings";
	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *
	 *
	 */
	// since the database has already cleaned to australia only.
	// db.listings.distinct("address.suburb");
	public List<String> getSuburbs(String country) {

		return template.findDistinct(new Query(), "address.suburb", LISTINGS_COLLECTION, String.class);
		// return null;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred }) 
	 *
	 *
	 */
	// db.listings.find(
	// {
	// 		"address.suburb": { $regex: /^<suburb>$/i },
	// 		accommodates: {$gte: <persons>},
	//      min_nights: {$gte: <duration>},
	// 		price: {$gte: 0, $lte: <priceRange>}
	// 	},
	// 	{ _id:1, name: 1, price: 1, accommodates : 1, }
	// )
	// .sort({ price: -1 });

	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		Query query = Query.query(
			new Criteria().andOperator(
				Criteria.where("address.suburb").regex(suburb, "i"),
				Criteria.where("accommodates").gte(persons),
				Criteria.where("min_nights").gte(duration),
				Criteria.where("price").gte(0).lte(priceRange)
			)
		);
		query.fields().include("_id","name", "accommodates", "price");
		query.with(Sort.by(Direction.DESC, "price"));

		return template.find(query, AccommodationSummary.class, LISTINGS_COLLECTION);
		// return null;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, LISTINGS_COLLECTION);
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
