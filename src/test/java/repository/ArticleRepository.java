
package repository;

        import com.example.demo.entity.Article;
        import org.springframework.data.jpa.repository.JpaRepository;

    public interface ArticleRepository extends JpaRepository<Article, Integer> {

    }