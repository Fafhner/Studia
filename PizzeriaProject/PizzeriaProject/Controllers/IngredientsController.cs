using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using PizzeriaProject.Data;
using PizzeriaProject.Models.Datas;
using PizzeriaProject.Models.Items;

namespace PizzeriaProject.Controllers
{
    [Route("[controller]/[action]")]
    public class IngredientsController : Controller
    {
        private readonly PizzasDbContext _context;

        public IngredientsController(PizzasDbContext context)
        {
            _context = context;
        }

        // GET: Ingredients
        [Route("/ingredients")]
        [Route("/ingredients/index")]
        public async Task<IActionResult> Index()
        {
            if (TempData["ErrorDelete"] != null)
            {
                ViewData["ErrorDelete"] = TempData["ErrorDelete"];
            }



            return View(await _context.Ingriedients.ToListAsync());
        }

        // GET: Ingredients/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var pizzaIngredientData = await _context.Ingriedients
                .FirstOrDefaultAsync(m => m.Id == id);
            if (pizzaIngredientData == null)
            {
                return NotFound();
            }

            return View(pizzaIngredientData);
        }

        // GET: Ingredients/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Ingredients/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Price")] PizzaIngredientData pizzaIngredientData, List<PizzaIngredientItem> ings)
        {
            if (ModelState.IsValid)
            {
                _context.Add(pizzaIngredientData);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(pizzaIngredientData);
        }

        // GET: Ingredients/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var pizzaIngredientData = await _context.Ingriedients.FindAsync(id);
            if (pizzaIngredientData == null)
            {
                return NotFound();
            }
            return View(pizzaIngredientData);
        }

        // POST: Ingredients/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Price")] PizzaIngredientData pizzaIngredientData)
        {
            if (id != pizzaIngredientData.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(pizzaIngredientData);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!PizzaIngredientDataExists(pizzaIngredientData.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            return View(pizzaIngredientData);
        }

        // GET: Ingredients/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }
            var pizzaIngredientData = await _context.Ingriedients
                .FirstOrDefaultAsync(m => m.Id == id);

            var ingredientsItems = _context.PizzaIngriedients
                .Include(pi => pi.Ingredient)
                .Where(pi => pi.IngredientId == id).Count();

            if (ingredientsItems != 0)
            {
                TempData["ErrorDelete"] = $"Cannot delete. Pizza with '{pizzaIngredientData.Name}' ingredient exist.";
                return RedirectToAction(nameof(Index));
            }

            if (pizzaIngredientData == null)
            {
                return NotFound();
            }

            return View(pizzaIngredientData);
        }

        // POST: Ingredients/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var pizzaIngredientData = await _context.Ingriedients.FindAsync(id);
            _context.Ingriedients.Remove(pizzaIngredientData);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PizzaIngredientDataExists(int id)
        {
            return _context.Ingriedients.Any(e => e.Id == id);
        }


        [AcceptVerbs("GET", "POST")]
        public IActionResult VerifyNameExist(string Name, int Id)
        {   
            foreach(var i in _context.Ingriedients.ToList())
            {
                if (i.Name == Name && i.Id != Id)
                {
                    return Json($"Name {Name} is already in use.");
                }
            }

            

            return Json(true);
        }

        [HttpPost]
        public IActionResult VerifyIngredientOnPizza(IEnumerable<int> ids)
        {
            var res = new List<int>();
            foreach(var id in ids)
            {
                var ingredientsItems = _context.PizzaIngriedients
                .Include(pi => pi.Ingredient)
                .Where(pi => pi.IngredientId == id)
                .Count();

                if (ingredientsItems != 0)
                {
                    res.Add(id);
                }

            }
            
            return Json(res);
        }
    }
}
