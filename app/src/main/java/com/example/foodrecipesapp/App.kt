package com.example.foodrecipesapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.foodrecipesapp.data.RecipesRepositoryImpl
import com.example.foodrecipesapp.data.UserRepositoryImpl
import com.example.foodrecipesapp.data.db.RecipesDatabase
import com.example.foodrecipesapp.data.db.UserData
import com.example.foodrecipesapp.data.db.model.CategorySW
import com.example.foodrecipesapp.data.db.model.ProductSW
import com.example.foodrecipesapp.data.db.model.RecipeProductsSW
import com.example.foodrecipesapp.data.db.model.RecipeSW
import com.example.foodrecipesapp.data.db.model.RecipeStepsSW
import com.example.foodrecipesapp.data.db.model.UserSW
import com.example.foodrecipesapp.domain.IRecipesRepository
import com.example.foodrecipesapp.domain.IUserRepository
import kotlinx.coroutines.runBlocking

class App: Application() {
    private lateinit var db: RecipesDatabase
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        lateinit var userRepository: IUserRepository
            private set
        lateinit var recipesRepository: IRecipesRepository
            private set
        lateinit var userManger: UserData
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initDB()
        initSharedPreferences()
        initUserManager()
        initUserRepository()
        initRecipesRepository()
        initData()
    }

    private fun initUserManager() {
        userManger = UserData(sharedPreferences)
    }

    private fun initSharedPreferences() {
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }


    private fun initDB() {
        db = Room.databaseBuilder(this, RecipesDatabase::class.java, "MovieDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initUserRepository() {
        userRepository = UserRepositoryImpl(db, userManger)
    }
    private fun initRecipesRepository() {
        recipesRepository = RecipesRepositoryImpl(db, userManger)
    }

    private fun initData() {
        var rcp: List<RecipeSW>
        runBlocking {
            rcp = db.recipesDao().getAllRecipes()
        }
        if (rcp.isEmpty()) {
            val user = UserSW(
                1,
                "admin",
                "admin@mail.ru",
                "Иван",
                "Петров",
                "+79999999999"
            )
            val categories = listOf(
                CategorySW(1, "Завтраки"),
                CategorySW(2, "Обеды"),
                CategorySW(3, "Ужины"),
                CategorySW(4, "Десерты"),
                CategorySW(5, "Закуски")
            )
            val products = listOf(
                ProductSW(1, "Яблоко", "https://img.freepik.com/free-vector/illustration-of-grapes-isolated-on-white-background_53876-17347.jpg", 52, 0, 0, 14),
                ProductSW(2, "Банан", "https://img.freepik.com/free-photo/single-banana-isolated-on-a-white-background_839833-17794.jpg?size=626&ext=jpg&ga=GA1.1.1546980028.1711065600&semt=ais", 96, 1, 0, 21),
                ProductSW(3, "Груша", "https://img.freepik.com/free-vector/illustration-of-apple-isolated-on-white-background_53876-17348.jpg", 57, 0, 0, 15),
                ProductSW(4, "Апельсин", "https://img.freepik.com/free-photo/orange-on-white-on-white_144627-16571.jpg", 43, 1, 0, 9),
                ProductSW(5, "Морковь", "https://img.freepik.com/premium-photo/png-fresh-carrot-isolated-on-white-background_185193-120017.jpg", 41, 0, 0, 10),
                ProductSW(6, "Картофель", "https://w7.pngwing.com/pngs/476/1004/png-transparent-potato-bag-vegetable-gunny-sack-food-potato-cooking-vegetables-peeler.png", 77, 2, 0, 17),
                ProductSW(7, "Томат", "https://w7.pngwing.com/pngs/305/581/png-transparent-plum-tomato-vegetarian-cuisine-bush-tomato-fruit-a-tomatoes-natural-foods-food-orange.png", 18, 1, 0, 4),
                ProductSW(8, "Огурец", "https://swlife.ru/image/cache/catalog/product/45/22/4522f806644286fb94d5c57ae8b7e790-0x0.webp", 15, 1, 0, 3),
                ProductSW(9, "Куриное филе", "https://img.freepik.com/premium-photo/chilled-chicken-fillet-isolated-on-a-white-background-close-up_182793-1455.jpg", 165, 31, 4, 0),
                ProductSW(10, "Говядина", "https://w7.pngwing.com/pngs/113/156/png-transparent-two-raw-meats-red-meat-beef-food-meat-roast-beef-chicken-meat-cooking.png", 250, 26, 17, 0),
                ProductSW(11, "Свинина", "https://w7.pngwing.com/pngs/425/915/png-transparent-pork-meat-veal-orloff-ribs-beef-frozen-fresh-sirloin-food-roast-beef-supermarket.png", 242, 25, 16, 0),
                ProductSW(12, "Рис", "https://sushibm.ru/wp-content/uploads/2020/03/%D1%82%D1%8F%D1%85%D0%B0%D0%BD%D1%80%D0%B8%D1%81.png", 130, 2, 0, 28),
                ProductSW(13, "Гречка", "https://png.pngtree.com/png-vector/20231110/ourmid/pngtree-black-belt-on-white-%D0%B8%D0%B7%D0%BE%D0%BB%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9-png-image_10422656.png", 132, 4, 1, 27),
                ProductSW(14, "Макароны", "https://img.lovepik.com/free-png/20220121/lovepik-spaghetti-png-image_401576607_wh860.png", 157, 5, 1, 30),
                ProductSW(15, "Хлеб", "https://w7.pngwing.com/pngs/986/398/png-transparent-slices-of-bread-bread-loaf-bread-baked-goods-food-bread.png", 265, 8, 3, 49),
                ProductSW(16, "Яйца", "https://img.freepik.com/free-photo/three-fresh-organic-raw-eggs-isolated-on-white-surface_114579-43677.jpg?size=626&ext=jpg&ga=GA1.1.1546980028.1711411200&semt=ais", 68, 6, 5, 1),
                ProductSW(17, "Масло", "https://w7.pngwing.com/pngs/164/248/png-transparent-butter-processed-cheese-milk-dairy-products-butter.png", 717, 1, 81, 0),
                ProductSW(18, "Лук", "https://static.vecteezy.com/system/resources/previews/023/961/416/non_2x/red-fresh-onion-free-png.png", 40, 1, 0, 9),
                ProductSW(19, "Чеснок", "https://static.vecteezy.com/system/resources/previews/027/214/959/original/garlic-garlic-garlic-transparent-background-ai-generated-free-png.png", 149, 6, 0, 33),
                ProductSW(20, "Фарш говяжий", "https://w7.pngwing.com/pngs/955/746/png-transparent-ground-meat-ribs-ground-beef-ground-meat-meat-food-beef-chicken-meat-thumbnail.png", 226, 16, 18, 0),
            )
            val recipes = listOf(
                RecipeSW(1, 1, "https://vkusnoitochka-meny.ru/wp-content/uploads/2022/11/main-67.png", "Яичница с тостами", 1, "Простой завтрак из яиц и тостов", "15 мин"),
                RecipeSW(2, 1, "https://img.lovepik.com/free-png/20211108/lovepik-spaghetti-bolognese-png-image_400541369_wh1200.png", "Спагетти Болоньезе", 2, "Классический обед из спагетти с мясным соусом", "30 мин"),
                RecipeSW(3, 1, "https://png.pngtree.com/png-vector/20240203/ourlarge/pngtree-chicken-fillet-with-vegetables-on-the-plate-png-image_11602520.png", "Куриное филе с овощами", 3, "Здоровый ужин из курицы и свежих овощей", "40 мин"),
                RecipeSW(4, 1, "https://paprikanta.com/wp-content/uploads/2022/09/Fruktovi-salat-osnovnoe-foto-1300x821.jpg", "Фруктовый салат", 4, "Легкий десерт из свежих фруктов", "15 мин"),
                RecipeSW(5, 1, "https://a-furshet.ru/mag/photo/norm131.jpg", "Сырная тарелка", 5, "Закуска из разных сортов сыра", "10 мин")
            )
            val recipeSteps = listOf(
                RecipeStepsSW(1, 1, 1, "Разбейте два яйца в миску."),
                RecipeStepsSW(2, 1, 2, "Взбейте яйца вилкой до однородности."),
                RecipeStepsSW(3, 1, 3, "Разогрейте сковороду и добавьте немного масла."),
                RecipeStepsSW(4, 1, 4, "Вылейте яйца на сковороду и жарьте, помешивая, до тех пор, пока они не будут готовы по вашему вкусу."),
                RecipeStepsSW(5, 1, 5, "Тосты поджарьте на тостере или на сковороде."),
                RecipeStepsSW(6, 1, 6, "Подавайте яичницу с тостами."),
                RecipeStepsSW(7, 2, 1, "Приготовьте ингредиенты: нарежьте лук, морковь, томаты и чеснок."),
                RecipeStepsSW(8, 2, 2, "Обжарьте лук и морковь на масле до золотистого цвета."),
                RecipeStepsSW(9, 2, 3, "Добавьте фарш и обжаривайте, до готовности."),
                RecipeStepsSW(10, 2, 4, "Добавьте томаты, чеснок, соль и специи. Тушите 15 минут."),
                RecipeStepsSW(11, 2, 5, "В это время отварите спагетти в соленой воде до состояния 'аль денте'."),
                RecipeStepsSW(12, 2, 6, "Смешайте спагетти с соусом и подавайте на стол.")
            )
            val recipeProducts = listOf(
                RecipeProductsSW(1, 16, 1, 100),
                RecipeProductsSW(2, 17, 1, 10),
                RecipeProductsSW(3, 15, 1, 50),
                RecipeProductsSW(4, 20, 2, 200), // 200 г говядины фарша для рецепта 2
                RecipeProductsSW(5, 18, 2, 100), // 100 г лука для рецепта 2
                RecipeProductsSW(6, 5, 2, 100), // 100 г моркови для рецепта 2
                RecipeProductsSW(7, 19, 2, 30),  // 30 г чеснока для рецепта 2
                RecipeProductsSW(8, 7, 2, 50),  // 50 г томатной пасты для рецепта 2
                RecipeProductsSW(9, 14, 2, 200),  // 200 г спагетти для рецепта 2
            )

            runBlocking {
                db.userDao().register(user)
                db.recipesDao().insertCategories(categories)
                db.recipesDao().insertProducts(products)
                db.recipesDao().insertRecipes(recipes)
                db.recipesDao().insertRecipeSteps(recipeSteps)
                db.recipesDao().insertRecipeProducts(recipeProducts)
            }
        }
    }
}