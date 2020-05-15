using Microsoft.EntityFrameworkCore;
using MvcNews.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MvcNews.Data
{
    public class NewsDbContext : DbContext
    {
        public NewsDbContext(DbContextOptions<NewsDbContext> options) : base(options)
        {
        }
        public DbSet<NewsItem> News { get; set; }

    }
}
