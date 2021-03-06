package com.littlefrog.respository;

import com.littlefrog.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author DW
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    @Query(value = "SELECT * from coupon  where user_id=? order by coupon_id desc ", nativeQuery = true)
    ArrayList<Coupon> findAllCoupon(int userID);

    @Query(value = "SELECT * from coupon  where coupon_id =? ", nativeQuery = true)
    Optional<Coupon> findOneCoupon(int couponID);

    @Transactional
    @Modifying
    @Query(value = "delete from coupon where coupon_id = ? ", nativeQuery = true)
    void deleteCoupon(int couponID);

    /**
     * 删除过期
     */
    @Transactional
    @Modifying
    @Query(value = "delete from coupon where dure_time < now()", nativeQuery = true)
    void deleteCoupons();


    @Query(value = "SELECT * from coupon  where user_id = ?1 and activity_code = ?2", nativeQuery = true)
    Optional<Coupon> findCouponByActivity(int userID, int code);
}
