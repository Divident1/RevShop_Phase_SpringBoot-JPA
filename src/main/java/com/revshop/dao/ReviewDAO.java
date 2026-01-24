package com.revshop.dao;

import com.revshop.model.Review;
import com.revshop.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (productId, userId, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getProductId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByProduct(int productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE productId = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review r = new Review();
                r.setReviewId(rs.getInt("reviewId"));
                r.setProductId(rs.getInt("productId"));
                r.setUserId(rs.getInt("userId"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setReviewDate(rs.getTimestamp("reviewDate"));
                reviews.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
