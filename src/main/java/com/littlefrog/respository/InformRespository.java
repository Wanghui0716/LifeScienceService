package com.littlefrog.respository;

import com.littlefrog.entity.Inform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;


/**
 * @author DW
 */
@Repository
public interface InformRespository extends JpaRepository<Inform, Integer> {
    @Query(value = "SELECT * from inform where(user_id=? or user_id=-1 ) order by inform_id desc ", nativeQuery = true)
    ArrayList<Inform> findAllInform(Integer userID);

    @Query(value = "SELECT * from inform where inform_id =? ", nativeQuery = true)
    Optional<Inform> findOneInform(Integer informID);

    @Transactional
    @Modifying
    @Query(value = "delete from inform where inform_id = ? ", nativeQuery = true)
    void deleteInform(Integer informID);

    @Transactional
    @Modifying
    @Query(value = "delete from inform where life_science.inform.user_id = ? ", nativeQuery = true)
    void deleteAllInform(int userID);

    @Transactional
    @Modifying
    @Query(value = "delete from life_science.inform where life_science.inform.category = ?1 and life_science.inform.return_id= ?2 ", nativeQuery = true)
    void deleteByCategoryID(int category, int returnID);

    @Transactional
    @Modifying
    @Query(value = "delete from life_science.inform where life_science.inform.user_id = -1 and life_science.inform.send_time < ?1", nativeQuery = true)
    void deleteInforms(Calendar sendDay);

}
