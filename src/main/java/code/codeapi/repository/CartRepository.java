package code.codeapi.repository;

import code.codeapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //사용자의 이메일을 통해서 Cart를 알아낸다.
    @Query("select cart from Cart cart where cart.owner.email = :email")
    public Optional<Cart> getCartOfMember(@Param("email") String email);
}
