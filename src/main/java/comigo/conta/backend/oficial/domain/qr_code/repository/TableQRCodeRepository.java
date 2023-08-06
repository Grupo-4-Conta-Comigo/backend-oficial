package comigo.conta.backend.oficial.domain.qr_code.repository;

import comigo.conta.backend.oficial.domain.qr_code.TableQRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableQRCodeRepository extends JpaRepository<TableQRCode, Long> {
    boolean existsByUserIdAndTable(String user_id, int table);
}
