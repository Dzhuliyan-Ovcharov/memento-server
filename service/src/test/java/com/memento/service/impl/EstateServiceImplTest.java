package java.com.memento.service.impl;

import com.memento.model.Estate;
import com.memento.repository.EstateRepository;
import com.memento.service.impl.EstateServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EstateServiceImplTest {

    @Mock
    private EstateRepository estateRepository;

    @InjectMocks
    private EstateServiceImpl estateService;

    @Test
    public void saveTest() {
        Estate estate = mock(Estate.class);
        when(estateRepository.save(any(Estate.class))).thenReturn(estate);
        estateService.save(estate);
        verify(estateRepository).save(any(Estate.class));
    }
}