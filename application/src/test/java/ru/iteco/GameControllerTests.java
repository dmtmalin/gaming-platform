package ru.iteco;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.iteco.game.*;
import ru.iteco.game.web.*;
import ru.iteco.profile.UserRepository;
import ru.iteco.profile.UserService;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(secure = false)
public class GameControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GameCategoryRepository gameCategoryRepository;
    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private GameVersionRepository gameVersionRepository;
    @MockBean
    private GameCreateFormValidator gameCreateFormValidator;
    @MockBean
    private GameChangeFormValidator gameChangeFormValidator;
    @MockBean
    private GameCreateForm gameCreateForm;
    @MockBean
    private GameChangeForm gameChangeForm;
    @MockBean
    private GameService gameService;
    @MockBean
    private GameCategoryService gameCategoryService;

    private TestUtility utility;

    private HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // supports any classes for validator
        when(gameCreateFormValidator.supports(any())).thenReturn(true);
        // csrf parameter
        httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        // test utility
        utility = new TestUtility();
    }

    @Test
    public void gameCreateSuccess() throws Exception {
        Integer gameId = 1;
        Game game = new Game();
        game.setId(gameId);
        // when create game need return only id
        when(gameService.create(isA(GameCreateForm.class))).thenReturn(game);
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test_name",
                "categoryId", "1",
                "description", "test_desc"
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk());
    }

    @Test
    public void gameCreateFieldsNullTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        String content = "";
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "name", "NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "categoryId", "NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "icon", "NotNull"))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "description", "NotNull"));
    }

    @Test
    public void gameCreateFieldNameNullTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "name", "NotNull"));
    }

    @Test
    public void gameCreateFieldNameEmptyTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "",
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "name", "Size"));

    }

    @Test
    public void gameCreateFieldNameOverSizeTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String name = generator.generate(256);
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", name,
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "name", "Size"));

    }

    @Test
    public void gameCreateFieldCategoryIdNullTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test_name",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "categoryId", "NotNull"));

    }

    @Test
    public void gameCreateFieldCategoryIdMismatchTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test_name",
                "categoryId", "bad",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "categoryId", "typeMismatch"));

    }

    @Test
    public void gameCreateFieldDescriptionNullTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1"
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "description", "NotNull"));

    }

    @Test
    public void gameCreateFieldDescriptionEmptyTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", ""
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "description", "Size"));
    }

    @Test
    public void gameCreateFieldDescriptionOverSizeTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(513);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "description", "Size"));
    }

    @Test
    public void gameCreateFieldIconNullTest() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", description
        );
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "icon", "NotNull"));
    }

    @Test
    public void gameCreateFieldIconNotSupportedFormat() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile(
                "icon", "icon.bad", MediaType.IMAGE_JPEG_VALUE, "image_test_bytes".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "icon", "SupportedFormat"));
    }

    @Test
    public void gameCreateFieldIconEmptyFile() throws Exception {
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile("icon", "icon.jpeg", "", new byte[0]);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "icon", "FileNotEmpty"));
    }

    @Test
    public void gameCreateFieldIconSmallResolution() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URI uri = classLoader.getResource("100x100.png").toURI();
        byte[] image = Files.readAllBytes(Paths.get(uri));
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String description = generator.generate(25);
        String content = utility.buildUrlEncodedFormEntity(
                "name", "test",
                "categoryId", "1",
                "description", description
        );
        MockMultipartFile icon = new MockMultipartFile("icon", "icon.jpeg", MediaType.IMAGE_JPEG_VALUE, image);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/game/create")
                .file(icon)
                .sessionAttr("_csrf", csrfToken)
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("game/game_create"))
                .andExpect(model().attributeExists("gameCreateForm"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasFieldErrorCode("gameCreateForm", "icon", "ImageMinResolution"));
    }

    @Test
    public void showGameTest() throws Exception {
        GameCategory gameCategory = new GameCategory();
        gameCategory.setId(1);
        gameCategory.setName("test");
        GameVersion gameVersion = new GameVersion();
        gameVersion.setBuild(1);
        gameVersion.setApprove(true);
        Game game = new Game();
        game.setName("test_name");
        game.setCreatedAt(new Date());
        game.setId(1);
        game.setDescription("test_desc");
        game.setGameCategory(gameCategory);
        game.setIcon("test_icon");
        game.setIconOrigin("test_icon_orig");
        game.setIconSmall("test_icon_small");
        game.setPublished(true);
        game.setCurrentGameVersion(gameVersion);


        when(gameRepository.findOne(game.getId())).thenReturn(game);

        when(gameCategoryRepository.findAllByOrderByPriorityDesc()).thenReturn(new ArrayList<GameCategory>() {
            {
                add(gameCategory);
            }});

        Map<String, String> categoriesMap = new HashMap<>();
        categoriesMap.put(
                gameCategory.getId().toString(),
                gameCategory.getName());

        mockMvc.perform(MockMvcRequestBuilders.get("/game/" + game.getId())
                .sessionAttr("mediaUrl", "/")
                .sessionAttr("flatBlock", utility.freemarkerEmptyMethod()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("game"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", categoriesMap));
    }
}
