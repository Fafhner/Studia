using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using PizzeriaProject.Data;
using PizzeriaProject.Models;
using PizzeriaProject.Models.Items;

namespace PizzeriaProject.Controllers
{
    [Route("[controller]/[action]")]
    public class PizzasController : Controller
    {
        private readonly PizzasDbContext _context;

        public PizzasController(PizzasDbContext context)
        {
            _context = context;
        }

        // GET: Pizzas
        [Route("/pizzas")]
        [Route("/pizzas/index")]
        public async Task<IActionResult> Index()
        {
            var pizzas = _context.Pizzas.Include(p => p.Dough).Include(p => p.Size).Include(p => p.Ingredients);

            var pizzaIngredients = _context.PizzaIngriedients.Include(i => i.Ingredient).Include(i => i.Pizza).ToList();

            return View(await pizzas.ToListAsync());
        }

        // GET: Pizzas/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var pizzaItem = await _context.Pizzas
                .Include(p => p.Dough)
                .Include(p => p.Size)
                .Include(p => p.Ingredients).ThenInclude(p => p.Ingredient)
                .FirstOrDefaultAsync(m => m.Id == id);

            if (pizzaItem == null)
            {
                return NotFound();
            }

            return View(pizzaItem);
        }

        // GET: Pizzas/Create
        public IActionResult Create()
        {

            ViewData["Doughs"] = _context.Doughs.ToList();
            ViewData["Sizes"] = _context.Sizes.ToList();
            ViewData["Ingredients"] = _context.Ingriedients.ToList();
            ViewData["RequiredQuantity"] = RequiredQuantity();

            return View();
        }

        // POST: Pizzas/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create( PizzaItem pizzaItem)
        {
            if (ModelState.IsValid)
            {
                pizzaItem.Ingredients = pizzaItem.Ingredients.Where(ing => ing.Quantity > 0).ToList();
                pizzaItem.Timestamp = DateTime.Now;
                _context.Add(pizzaItem);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["Doughs"] = _context.Doughs.ToList();
            ViewData["Sizes"] = _context.Sizes.ToList();
            ViewData["Ingredients"] = _context.Ingriedients.OrderBy(i=>i.Id).ToList();
            ViewData["RequiredQuantity"] = RequiredQuantity();
            return View(pizzaItem);
        }

        // GET: Pizzas/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var pizzaItem = await _context.Pizzas.FindAsync(id);
            if (pizzaItem == null)
            {
                return NotFound();
            }
            ViewData["DoughId"] = new SelectList(_context.Doughs, "Id", "Type", pizzaItem.DoughId);
            ViewData["SizeId"] = new SelectList(_context.Sizes, "Id", "Size", pizzaItem.SizeId);
            return View(pizzaItem);
        }

       
        // GET: Pizzas/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var pizzaItem = await _context.Pizzas
             .Include(p => p.Dough)
             .Include(p => p.Size)
             .Include(p => p.Ingredients).ThenInclude(p => p.Ingredient)
             .FirstOrDefaultAsync(m => m.Id == id); 

            if (pizzaItem == null)
            {
                return NotFound();
            }

            return View(pizzaItem);
        }

        // POST: Pizzas/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var ingredients = await _context.PizzaIngriedients.Where(i => i.PizzaId == id).ToListAsync();

            _context.PizzaIngriedients.RemoveRange(ingredients);

            var pizzaItem = await _context.Pizzas.FindAsync(id);
            _context.Pizzas.Remove(pizzaItem);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool PizzaItemExists(int id)
        {
            return _context.Pizzas.Any(e => e.Id == id);
        }


        [AcceptVerbs("GET", "POST")]
        public IActionResult VerifyNameExist(string Name)
        {
            List<string> names = _context.Pizzas.Select(p => p.Name).ToList();

            if (names.Contains(Name))
            {
                return Json($"Name {Name} is already in use.");
            }

            return Json(true);
        }

        private Dictionary<int, int> RequiredQuantity()
        {
            var req = new Dictionary<int, int>();
            foreach(var ing in _context.Ingriedients)
            {
                if (ing.Name == "Cheese" || ing.Name == "Tomato sauce") {
                    req[ing.Id] = 1;
                }
                else
                {
                    req[ing.Id] = 0;
                }
                
            }
            return req;
        }
    }
}
