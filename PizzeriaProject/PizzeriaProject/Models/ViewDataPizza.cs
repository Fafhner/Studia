using PizzeriaProject.Models.Datas;
using PizzeriaProject.Models.Items;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Models
{
    public class ViewDataPizza
    {
        public PizzaItem Pizza { set; get; }
        public List<PizzaIngredientItem> PizzasIngredients {set;get;}
    }
}
