using Microsoft.EntityFrameworkCore;
using PizzeriaProject.Models;
using PizzeriaProject.Models.Datas;
using PizzeriaProject.Models.Items;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Data
{

    public class PizzasDbContext : DbContext
    {
        public PizzasDbContext(DbContextOptions<PizzasDbContext> options) : base(options)
        { }

        // Datas
        public DbSet<PizzaIngredientData> Ingriedients { get; set; }
        public DbSet<PizzaDoughData> Doughs { get; set; }
        public DbSet<PizzaSizeData> Sizes { get; set; }

        // Items
        public DbSet<PizzaItem> Pizzas { get; set; }
        public DbSet<PizzaIngredientItem> PizzaIngriedients { get; set; }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

            modelBuilder.Entity<PizzaDoughData>().HasData(
                new PizzaDoughData { Id = 1, Type = "Thin-crust", Price = 3.0m },
                new PizzaDoughData { Id = 2, Type = "Thick-crust", Price = 4.0m });

            modelBuilder.Entity<PizzaSizeData>().HasData(
                new PizzaSizeData { Id = 1, Size = "Small", Price = 3.0m },
                new PizzaSizeData { Id = 2, Size = "Medium", Price = 5.0m },
                new PizzaSizeData { Id = 3, Size = "Big", Price = 7.0m });

            modelBuilder.Entity<PizzaIngredientData>().HasData(
                new PizzaIngredientData { Id = 1, Name = "Cheese", Price = 2.0m },
                new PizzaIngredientData { Id = 2, Name = "Ham", Price = 5.0m },
                new PizzaIngredientData { Id = 3, Name = "Chicken", Price = 5.0m },
                new PizzaIngredientData { Id = 4, Name = "Corn", Price = 3.0m },
                new PizzaIngredientData { Id = 5, Name = "Sausage", Price = 6.0m },
                new PizzaIngredientData { Id = 6, Name = "Tomato sauce", Price = 1.0m },
                 new PizzaIngredientData { Id = 7, Name = "Green pepper", Price = 2.0m },
                 new PizzaIngredientData { Id = 8, Name = "Red pepper", Price = 2.0m },
                 new PizzaIngredientData { Id = 9, Name = "Onion", Price = 2.0m },
                 new PizzaIngredientData { Id = 10, Name = "Mushrooms", Price = 2.0m });


            modelBuilder.Entity<PizzaIngredientItem>().HasData(
                new PizzaIngredientItem { Id = 1, Quantity = 3, IngredientId = 1, PizzaId = 1 },
                new PizzaIngredientItem { Id = 2, Quantity = 1, IngredientId = 2, PizzaId = 1 },
                new PizzaIngredientItem { Id = 3, Quantity = 1, IngredientId = 3, PizzaId = 1 },
                new PizzaIngredientItem { Id = 4, Quantity = 1, IngredientId = 6, PizzaId = 1 });

            modelBuilder.Entity<PizzaItem>().HasData(
                new PizzaItem { Id = 1, Name = "SuperPizza", DoughId = 2, SizeId = 3, Timestamp = DateTime.Now });

        }

    }
}
