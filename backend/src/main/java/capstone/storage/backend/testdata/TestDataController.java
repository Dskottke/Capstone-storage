package capstone.storage.backend.testdata;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test-data")
@RequiredArgsConstructor
public class TestDataController {

  private final TestDataService testDataService;

  @PostMapping()
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addTestData() {
    testDataService.addTestData();
  }

}
