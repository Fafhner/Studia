using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
namespace StudentsApi.Models
{
    public class Student
    {
        public long Id { get; set; }
        public int Index { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
    }
}