package live.chat.live_chat.model;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import  live.chat.live_chat.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
